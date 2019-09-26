package com.bcauction.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Auction
{
	private long id;
	private long 경매생성자id; //회원id
	private long 경매작품id;
	private LocalDateTime 생성일시;
	private String 상태 = "V"; // V valid(유효함), C canceled, E ended
	private LocalDateTime 시작일시;
	private LocalDateTime 종료일시;
	private BigInteger 최저가;
	private String 컨트랙트주소;

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public long get경매생성자id()
	{
		return 경매생성자id;
	}

	public void set경매생성자id(final long 경매생성자id)
	{
		this.경매생성자id = 경매생성자id;
	}

	public long get경매작품id()
	{
		return 경매작품id;
	}

	public void set경매작품id(final long 경매작품id)
	{
		this.경매작품id = 경매작품id;
	}

	public LocalDateTime get생성일시()
	{
		return 생성일시;
	}

	public void set생성일시(final LocalDateTime 생성일시)
	{
		this.생성일시 = 생성일시;
	}

	public String get상태()
	{
		return 상태;
	}

	public void set상태(final String 상태)
	{
		this.상태 = 상태;
	}

	public LocalDateTime get시작일시()
	{
		return 시작일시;
	}

	public void set시작일시(final LocalDateTime 시작일시)
	{
		this.시작일시 = 시작일시;
	}

	public LocalDateTime get종료일시()
	{
		return 종료일시;
	}

	public void set종료일시(final LocalDateTime 종료일시)
	{
		this.종료일시 = 종료일시;
	}

	public BigInteger get최저가()
	{
		return 최저가;
	}

	public void set최저가(final BigInteger 최저가)
	{
		this.최저가 = 최저가;
	}

	public String get컨트랙트주소()
	{
		return 컨트랙트주소;
	}

	public void set컨트랙트주소(final String 컨트랙트주소)
	{
		this.컨트랙트주소 = 컨트랙트주소;
	}

	@Override
	public String toString() {
		return "Auction [id=" + id + ", 경매생성자id=" + 경매생성자id + ", 경매작품id=" + 경매작품id + ", 생성일시=" + 생성일시 + ", 상태=" + 상태
				+ ", 시작일시=" + 시작일시 + ", 종료일시=" + 종료일시 + ", 최저가=" + 최저가 + ", 컨트랙트주소=" + 컨트랙트주소 + "]";
	}
	
}
