package com.chris.dg_data.threadpool;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther Chris Lee
 * @Date 12/19/2018 15:48
 * @Description
 */
public class BlockingPolicyHandler implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		try {
			executor.getQueue().put(r);
		}
		catch (InterruptedException var4) {
			throw new RejectedExecutionException("Unexpected InterruptedException", var4);
		}
		
	}
}
