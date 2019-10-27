package com.chris.dg_data.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.util.Objects.requireNonNull;

/**
 * @Auther Chris Lee
 * @Date 12/19/2018 15:34
 * @Description
 */
public class NamedDaemonThreadFactory implements ThreadFactory {
	
	private final ThreadFactory defaultThreadFactory;
	
	private final String name;
	
	public NamedDaemonThreadFactory(String name)
	{
		this(Executors.defaultThreadFactory(), name);
	}
	
	NamedDaemonThreadFactory(ThreadFactory defaultThreadFactory, String name)
	{
		requireNonNull(defaultThreadFactory);
		requireNonNull(name);
		this.defaultThreadFactory = defaultThreadFactory;
		this.name = name;
	}
	
    @Override
	public Thread newThread(Runnable r) {
		Thread t = defaultThreadFactory.newThread(r);
		if (!t.isDaemon()) {
			t.setDaemon(true);
		}
		if (t.getName().startsWith("pool-")) {
			t.setName(name + t.getName().substring(4));
		}
		return t;
	}
}
