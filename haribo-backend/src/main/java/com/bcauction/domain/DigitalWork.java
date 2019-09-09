package com.bcauction.domain;

public class DigitalWork
{
	private long id;
	private String 이름;
	private String 설명;
	private String 공개여부 = "Y";
	private String 상태 = "Y";
	private long 회원id;

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String get이름()
	{
		return 이름;
	}

	public void set이름(final String 이름)
	{
		this.이름 = 이름;
	}

	public String get설명()
	{
		return 설명;
	}

	public void set설명(final String 설명)
	{
		this.설명 = 설명;
	}

	public String get공개여부()
	{
		return 공개여부;
	}

	public void set공개여부(final String 공개여부)
	{
		this.공개여부 = 공개여부;
	}

	public String get상태()
	{
		return 상태;
	}

	public void set상태(final String 상태)
	{
		this.상태 = 상태;
	}

	public long get회원id()
	{
		return 회원id;
	}

	public void set회원id(final long 회원id)
	{
		this.회원id = 회원id;
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
				.append("{ id: " + id)
				.append("\n\t이름: " + 이름)
				.append("\n\t설명: " + 설명)
				.append("\n\t공개여부: " + 공개여부)
				.append("\n\t상태: " + 상태)
				.append("\n\t회원id: " + 회원id)
				.append(" }")
				.toString();
	}
}
