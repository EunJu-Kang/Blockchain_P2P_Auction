package com.bcauction.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 패브릭 체인코드로부터 조회된 결과를 담기위한 클래스
 */
public class FabricAsset
{
	private static final Logger logger = LoggerFactory.getLogger(FabricAsset.class);

	private String assetId;
	private String owner;
	private LocalDateTime createdAt;
	private LocalDateTime expiredAt;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public String getAssetId()
	{
		return assetId;
	}

	public void setAssetId(final String assetId)
	{
		this.assetId = assetId;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(final String owner)
	{
		this.owner = owner;
	}

	public LocalDateTime getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(final String createdAt)
	{
		if("FALSE".equals(createdAt))
			this.createdAt = null;
		else
			this.createdAt = LocalDateTime.parse(createdAt, formatter);
	}

	public LocalDateTime getExpiredAt()
	{
		return expiredAt;
	}

	public void setExpiredAt(final String expiredAt)
	{
		if("FALSE".equals(expiredAt))
			this.expiredAt = null;
		else
			this.expiredAt = LocalDateTime.parse(expiredAt, formatter);
	}
}
