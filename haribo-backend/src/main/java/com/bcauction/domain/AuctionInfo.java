package com.bcauction.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class AuctionInfo
{
	private String 경매컨트랙트주소;
	private BigInteger 최고입찰액;
	private long 작품id;
	private long 최고입찰자id;
	private LocalDateTime 경매시작시간;
	private LocalDateTime 경매종료시간;
	private BigInteger 최소금액;
	private boolean 종료;

	public String get경매컨트랙트주소()
	{
		return 경매컨트랙트주소;
	}

	public void set경매컨트랙트주소(final String 경매컨트랙트주소)
	{
		this.경매컨트랙트주소 = 경매컨트랙트주소;
	}

	public BigInteger get최고입찰액()
	{
		return 최고입찰액;
	}

	public void set최고입찰액(final BigInteger 최고입찰액)
	{
		this.최고입찰액 = 최고입찰액;
	}

	public long get최고입찰자id()
	{
		return 최고입찰자id;
	}

	public void set최고입찰자id(final long 최고입찰자id)
	{
		this.최고입찰자id = 최고입찰자id;
	}


	public LocalDateTime get경매시작시간()
	{
		return 경매시작시간;
	}

	public void set경매시작시간(final LocalDateTime 경매시작시간)
	{
		this.경매시작시간 = 경매시작시간;
	}

	public LocalDateTime get경매종료시간()
	{
		return 경매종료시간;
	}

	public void set경매종료시간(final LocalDateTime 경매종료시간)
	{
		this.경매종료시간 = 경매종료시간;
	}

	public BigInteger get최소금액()
	{
		return 최소금액;
	}

	public void set최소금액(final BigInteger 최소금액)
	{
		this.최소금액 = 최소금액;
	}

	public boolean is종료()
	{
		return 종료;
	}

	public void set종료(final boolean 종료)
	{
		this.종료 = 종료;
	}

	public long get작품id()
	{
		return 작품id;
	}

	public void set작품id(final long 작품id)
	{
		this.작품id = 작품id;
	}
}
