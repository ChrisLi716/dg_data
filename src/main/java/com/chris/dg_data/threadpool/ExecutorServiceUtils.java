package com.chris.dg_data.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @Auther Chris Lee
 * @Date 12/19/2018 16:04
 * @Description
 */
public class ExecutorServiceUtils {
	
	/**
	 * Safe shutdown as recommended in {@link ExecutorService} docs.
	 */
	static List<Runnable> shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown();
		try {
			if (!pool.awaitTermination(5, SECONDS)) {
				return pool.shutdownNow();
			}
		}
		catch (InterruptedException e) {
			List<Runnable> dangling = pool.shutdownNow();
			Thread.currentThread().interrupt();
			return dangling;
		}
		return emptyList();
	}
}
