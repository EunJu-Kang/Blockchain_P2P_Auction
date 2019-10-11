package com.bcauction.application.impl;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bcauction.application.IFabricCCService;
import com.bcauction.domain.CommonUtil;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.FabricUser;

@Service
public class FabricCCService implements IFabricCCService {
	private static final Logger logger = LoggerFactory.getLogger(FabricCCService.class);

	private HFClient hfClient;
	private Channel channel;

	@Value("${fabric.ca-server.url}")
	private String CA_SERVER_URL;
	@Value("${fabric.ca-server.admin.name}")
	private String CA_SERVER_ADMIN_NAME;
	@Value("${fabric.ca-server.pem.file}")
	private String CA_SERVER_PEM_FILE;
	@Value("${fabric.org.name}")
	private String ORG_NAME;
	@Value("${fabric.org.msp.name}")
	private String ORG_MSP_NAME;
	@Value("${fabric.org.admin.name}")
	private String ORG_ADMIN_NAME;
	@Value("${fabric.peer.name}")
	private String PEER_NAME;
	@Value("${fabric.peer.url}")
	private String PEER_URL;
	@Value("${fabric.peer.pem.file}")
	private String PEER_PEM_FILE;
	@Value("${fabric.orderer.name}")
	private String ORDERER_NAME;
	@Value("${fabric.orderer.url}")
	private String ORDERER_URL;
	@Value("${fabric.orderer.pem.file}")
	private String ORDERER_PEM_FILE;
	@Value("${fabric.org.user.name}")
	private String USER_NAME;
	@Value("${fabric.org.user.secret}")
	private String USER_SECRET;
	@Value("${fabric.channel.name}")
	private String CHANNEL_NAME;

	private void loadChannel() {
		try {
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite(); 
			Properties properties = getPropertiesWith(CA_SERVER_PEM_FILE);

			HFCAClient caClient = HFCAClient.createNewInstance(CA_SERVER_URL, properties);
			caClient.setCryptoSuite(cryptoSuite);

			Enrollment adminEnrollment = caClient.enroll(USER_NAME, USER_SECRET);
			FabricUser admin = new FabricUser(USER_NAME, ORG_NAME, ORG_MSP_NAME, adminEnrollment);

			this.hfClient = HFClient.createNewInstance();
			this.hfClient.setCryptoSuite(cryptoSuite);
			this.hfClient.setUserContext(admin);

			Peer peer = hfClient.newPeer(PEER_NAME, PEER_URL);
			Orderer orderer = hfClient.newOrderer(ORDERER_NAME, ORDERER_URL);
			this.channel = hfClient.newChannel(CHANNEL_NAME);
			this.channel.addPeer(peer);
			this.channel.addOrderer(orderer);
			this.channel.initialize();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Properties getPropertiesWith(String filename) {
		Properties properties = new Properties();
		properties.put("pemBytes", CommonUtil.readString(filename).getBytes());
		properties.setProperty("sslProvider", "openSSL");
		properties.setProperty("negotiationType", "TLS");
		return properties;
	}

	@Override
	public FabricAsset registerOwnership(final long 소유자, final long 작품id) {
		if (this.channel == null || this.hfClient == null)
			loadChannel();
		boolean res = registerAsset(작품id, 소유자);
		if (!res)
			return null;
		res = confirmTimestamp(작품id);
		if (!res)
			return null;
		return query(작품id);
	}

	@Override
	public List<FabricAsset> transferOwnership(final long from, final long to, final long 작품id) {
	      if (this.channel == null)
	         loadChannel();

	      List<FabricAsset> assets = new ArrayList<>();
	      boolean res = this.expireAssetOwnership(작품id, from);
	      if (!res)
	         return null;
	      FabricAsset expired = query(작품id);
	      if (expired == null)
	         return null;
	      assets.add(expired);

	      res = this.updateAssetOwnership(작품id, to);
	      if (!res)
	         return null;
	      FabricAsset transferred = query(작품id);
	      if (transferred == null)
	         return null;
	      assets.add(transferred);

	      return assets;
	}

	@Override
	public FabricAsset expireOwnership(final long 작품id, final long 소유자id) {
		if (this.channel == null)
			loadChannel();
		boolean res = this.expireAssetOwnership(작품id, 소유자id);
		if (!res)
			return null;

		res = confirmTimestamp(작품id);
		if (!res)
			return null;

		return query(작품id);
	}

	private boolean registerAsset(final long 작품id, final long 소유자) {
		if (this.hfClient == null || this.channel == null)
			loadChannel();

		String[] args = { 작품id + "", 소유자 + "" };
		boolean result = false;

		QueryByChaincodeRequest qpr = this.hfClient.newQueryProposalRequest();
		ChaincodeID fabricCCId = ChaincodeID.newBuilder().setName("asset").build();
		qpr.setChaincodeID(fabricCCId);
		qpr.setFcn("registerAsset");
		qpr.setArgs(args);
		Collection<ProposalResponse> res;
		try {
			res = this.channel.queryByChaincode(qpr);
			CompletableFuture<TransactionEvent> tmp = this.channel.sendTransaction(res);
			result = tmp.get().isValid();
		} catch (org.hyperledger.fabric.sdk.exception.InvalidArgumentException e) {
			e.printStackTrace();
		} catch (ProposalException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return result;
	}

	private boolean confirmTimestamp(final long 작품id) {
		if (this.hfClient == null || this.channel == null)
			loadChannel();

		String[] args = { 작품id + "" };
		boolean result = false;

		TransactionProposalRequest tpr = this.hfClient.newTransactionProposalRequest();
		ChaincodeID fabricCCId = ChaincodeID.newBuilder().setName("asset").build();
		tpr.setChaincodeID(fabricCCId);
		tpr.setFcn("confirmTimestamp");
		tpr.setArgs(args);

		Collection<ProposalResponse> res;
		try {
			res = this.channel.sendTransactionProposal(tpr, this.channel.getPeers());
			CompletableFuture<TransactionEvent> tmp = this.channel.sendTransaction(res);
			result = tmp.get().isValid();
		} catch (org.hyperledger.fabric.sdk.exception.InvalidArgumentException e) {
			e.printStackTrace();
		} catch (ProposalException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean expireAssetOwnership(final long 작품id, final long 소유자) {
		if (this.hfClient == null || this.channel == null)
			loadChannel();

		String[] args = { 작품id + "", 소유자 + "" };
		boolean result = false;

		QueryByChaincodeRequest qpr = this.hfClient.newQueryProposalRequest();
		ChaincodeID fabricCCId = ChaincodeID.newBuilder().setName("asset").build();
		qpr.setChaincodeID(fabricCCId);
		qpr.setFcn("expireAssetOwnership");
		qpr.setArgs(args);

		Collection<ProposalResponse> res;
		try {
			res = this.channel.queryByChaincode(qpr);
			CompletableFuture<TransactionEvent> tmp = this.channel.sendTransaction(res);
			result = tmp.get().isValid();
		} catch (org.hyperledger.fabric.sdk.exception.InvalidArgumentException e) {
			e.printStackTrace();
		} catch (ProposalException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean updateAssetOwnership(final long 작품id, final long to) {
		if (this.hfClient == null || this.channel == null)
			loadChannel();

		String[] args = { 작품id + "", to + "" };
		boolean result = false;

		QueryByChaincodeRequest qpr = this.hfClient.newQueryProposalRequest();
		ChaincodeID fabricCCId = ChaincodeID.newBuilder().setName("asset").build();
		qpr.setChaincodeID(fabricCCId);
		qpr.setFcn("updateAssetOwnership");
		qpr.setArgs(args);

		Collection<ProposalResponse> res;
		try {
			res = this.channel.queryByChaincode(qpr);
			CompletableFuture<TransactionEvent> tmp = this.channel.sendTransaction(res);
			result = tmp.get().isValid();
		} catch (org.hyperledger.fabric.sdk.exception.InvalidArgumentException e) {
			e.printStackTrace();
		} catch (ProposalException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<FabricAsset> queryHistory(final long 작품id) {
		if (this.hfClient == null || this.channel == null)
			loadChannel();
		
		List<FabricAsset> history = new ArrayList<>();
		String[] args = { 작품id + "" };
		String stringResponse = "";

		QueryByChaincodeRequest qpr = this.hfClient.newQueryProposalRequest();
		ChaincodeID fabricCCId = ChaincodeID.newBuilder().setName("asset").build();
		qpr.setChaincodeID(fabricCCId);
		qpr.setFcn("getAssetHistory");
		qpr.setArgs(args);

		Collection<ProposalResponse> res;
		ByteArrayInputStream bais = null;

		try {
			res = this.channel.queryByChaincode(qpr);
			for (ProposalResponse pres : res) {
				stringResponse = new String(pres.getChaincodeActionResponsePayload());

				JsonReader jsonReader = Json.createReader(new StringReader(stringResponse));
				JsonArray ja = jsonReader.readArray();
				jsonReader.close();

				for (int i = 0; i < ja.size(); i++) {
					JsonObject object = ja.get(i).asJsonObject();
					FabricAsset fa = this.getAssetRecord(object);
					history.add(fa);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return history;
	}

	@Override
	public FabricAsset query(final long 작품id) {
		if (this.hfClient == null || this.channel == null)
			loadChannel();
		FabricAsset asset = new FabricAsset();
		String stringasset = null;

		String[] args = { 작품id + "" };

		QueryByChaincodeRequest qpr = this.hfClient.newQueryProposalRequest();
		ChaincodeID fabBoardCCId = ChaincodeID.newBuilder().setName("asset").build();
		qpr.setChaincodeID(fabBoardCCId);
		qpr.setFcn("query");
		qpr.setArgs(args);

		try {
			Collection<ProposalResponse> res = this.channel.queryByChaincode(qpr);
			for (ProposalResponse pres : res) {
				stringasset = new String(pres.getChaincodeActionResponsePayload());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(stringasset);
				JSONObject jsonObj = (JSONObject) obj;
				asset.setAssetId((String) jsonObj.get("assetID"));
				asset.setOwner((String) jsonObj.get("owner"));
				asset.setCreatedAt((String) jsonObj.get("createdAt"));
				asset.setExpiredAt((String) jsonObj.get("expiredAt"));

			}
		} catch (org.hyperledger.fabric.sdk.exception.InvalidArgumentException | ProposalException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return asset;
	}

	private static FabricAsset getAssetRecord(final JsonObject rec) {
		FabricAsset asset = new FabricAsset();
		asset.setAssetId(rec.getString("assetID"));
		asset.setOwner(rec.getString("owner"));
		asset.setCreatedAt(rec.getString("createdAt"));
		asset.setExpiredAt(rec.getString("expiredAt"));
		
		return asset;
	}

}
