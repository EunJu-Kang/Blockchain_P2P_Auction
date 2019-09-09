package com.bcauction.domain;

import java.time.LocalDateTime;

public class Ownership
{
	private long id;
	private long 소유자id;
	private long 작품id;
	private LocalDateTime 소유시작일자;
	private LocalDateTime 소유종료일자;

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public long get소유자id()
	{
		return 소유자id;
	}

	public void set소유자id(final long 소유자id)
	{
		this.소유자id = 소유자id;
	}

	public long get작품id()
	{
		return 작품id;
	}

	public void set작품id(final long 작품id)
	{
		this.작품id = 작품id;
	}

	public LocalDateTime get소유시작일자()
	{
		return 소유시작일자;
	}

	public void set소유시작일자(final LocalDateTime 소유시작일자)
	{
		this.소유시작일자 = 소유시작일자;
	}

	public LocalDateTime get소유종료일자()
	{
		return 소유종료일자;
	}

	public void set소유종료일자(final LocalDateTime 소유종료일자)
	{
		this.소유종료일자 = 소유종료일자;
	}
}
