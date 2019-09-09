package com.bcauction.domain;

import java.math.BigDecimal;

public class Wallet
{
	private long id;
	private long 소유자id;
	private String 주소;
	private BigDecimal 잔액 = BigDecimal.valueOf(0);
	private int 충전회수 = 0;

	public long get소유자id()
	{
		return 소유자id;
	}

	public void set소유자id(final long 소유자id)
	{
		this.소유자id = 소유자id;
	}

	public String get주소()
	{
		return 주소;
	}

	public void set주소(final String 주소)
	{
		this.주소 = 주소;
	}

	public BigDecimal get잔액()
	{
		return 잔액;
	}

	public void set잔액(final BigDecimal 잔액)
	{
		this.잔액 = 잔액;
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public int get충전회수()
	{
		return 충전회수;
	}

	public void set충전회수(final int 충전회수)
	{
		this.충전회수 = 충전회수;
	}

	public boolean 충전가능(){
		return this.충전회수 < 10;
	}
}
