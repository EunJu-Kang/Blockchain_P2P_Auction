package com.bcauction.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid
{
	private long id;
	private long 경매참여자id;
	private long 경매id;
	private LocalDateTime 입찰일시;
	private BigDecimal 입찰금액;
	private String 낙찰여부 = "N";

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public long get경매참여자id()
	{
		return 경매참여자id;
	}

	public void set경매참여자id(final long 경매참여자id)
	{
		this.경매참여자id = 경매참여자id;
	}

	public long get경매id()
	{
		return 경매id;
	}

	public void set경매id(final long 경매id)
	{
		this.경매id = 경매id;
	}

	public LocalDateTime get입찰일시()
	{
		return 입찰일시;
	}

	public void set입찰일시(final LocalDateTime 입찰일시)
	{
		this.입찰일시 = 입찰일시;
	}

	public BigDecimal get입찰금액()
	{
		return 입찰금액;
	}

	public void set입찰금액(final BigDecimal 입찰금액)
	{
		this.입찰금액 = 입찰금액;
	}

	public String get낙찰여부()
	{
		return 낙찰여부;
	}

	public void set낙찰여부(final String 낙찰여부)
	{
		this.낙찰여부 = 낙찰여부;
	}
}
