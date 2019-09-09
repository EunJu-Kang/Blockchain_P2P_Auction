package com.bcauction.domain;

import org.web3j.tuples.generated.Tuple7;

import java.math.BigInteger;

public class AuctionInfoFactory {

    public static AuctionInfo 생성(String 컨트랙트주소, long 지갑소유자Id,
            Tuple7<BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, Boolean> info) {
        AuctionInfo auctionInfo = new AuctionInfo();
        auctionInfo.set경매컨트랙트주소(컨트랙트주소);
        auctionInfo.set경매시작시간(CommonUtil.ETH타임스탬프변환(info.getValue1().longValue()));
        auctionInfo.set경매종료시간(CommonUtil.ETH타임스탬프변환(info.getValue2().longValue()));
        auctionInfo.set최소금액(info.getValue3());
        auctionInfo.set작품id(info.getValue4().longValue());

        auctionInfo.set최고입찰자id(지갑소유자Id);
        auctionInfo.set최고입찰액(info.getValue6());
        auctionInfo.set종료(info.getValue7());

        return auctionInfo;
    }
}
