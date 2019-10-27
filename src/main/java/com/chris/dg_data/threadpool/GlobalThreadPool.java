package com.chris.dg_data.threadpool;

import java.util.List;
import java.util.concurrent.*;

import static com.chris.dg_data.threadpool.ExecutorServiceUtils.shutdownAndAwaitTermination;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @Auther Chris Lee
 * @Date 12/19/2018 15:30
 * @Description
 */
public class GlobalThreadPool {

	private static final int corePoolSize = 10 * Runtime.getRuntime().availableProcessors();

	private static final int maxPoolSize = corePoolSize * 2;

	private static final int maxQueuedTasks = 256;

	private final BlockingQueue<Runnable> backingQueue;

	private final ExecutorService pool;

	private static final class InstanceHolder {
		static final GlobalThreadPool INSTANCE = new GlobalThreadPool();
	}

	private static volatile boolean gotInstance;

	private GlobalThreadPool() {
		this.backingQueue = new ArrayBlockingQueue<>(maxQueuedTasks);
		this.pool = new ThreadPoolExecutor(corePoolSize,
			maxPoolSize,
			60L,
			SECONDS,
			backingQueue,
			new NamedDaemonThreadFactory("GlobalThreadPool"),
			new BlockingPolicyHandler());
	}

	public static GlobalThreadPool getInstance() {
		gotInstance = true;
		return InstanceHolder.INSTANCE;
	}

	/**
	 * If you call this, you will not be able to start it again. Only do this when the servlet is shutting down.
	 */
	public static void shutdown() {
		if (gotInstance) {
			List<Runnable> dangling = shutdownAndAwaitTermination(getInstance().getPool());
			if (!dangling.isEmpty()) {
				System.out.println(dangling.size() + " tasks in queue were not run");
			}
		}
	}

	public ExecutorService getPool() {
		return pool;
	}

	public static void execute(Runnable task) {
		getInstance().getPool().execute(task);
	}

	public static <T> Future<T> execute(Callable<T> task) {
		return getInstance().getPool().submit(task);
	}
}
