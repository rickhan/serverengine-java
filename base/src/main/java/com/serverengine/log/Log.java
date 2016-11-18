package com.serverengine.log;

public class Log {

	public static void error(Object... args)
	{
		System.err.println(args);
	}
	
	public static void info(Object... args)
	{
		System.out.println(args);
	}
	
	public static void debug(Object... args)
	{
		System.out.println(args);
	}
}
