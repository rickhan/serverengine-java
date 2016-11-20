package com.serverengine.timer;

import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/**
 * 定时器
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class TimerMgr {
	/**
	 * 定时器
	 */
	private static Timer timer = new HashedWheelTimer();
	static {
		((HashedWheelTimer)timer).start();
	}

}
