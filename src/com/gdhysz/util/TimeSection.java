package com.gdhysz.util;

public class TimeSection
{
	long startTime = -1;
	long endTime = -1;

	private static TimeSection instance = null;

	public static TimeSection getInstance()
	{
		if (instance == null)
		{
			synchronized (TimeSection.class)
			{
				if (instance == null)
				{
					instance = new TimeSection();
				}
			}
		}
		return instance;
	}

	public TimeSection()
	{
		super();
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	@Override
	public String toString()
	{
		return "TimeSection [startTime=" + startTime + ", endTime=" + endTime
				+ "]";
	}

	
	
}
