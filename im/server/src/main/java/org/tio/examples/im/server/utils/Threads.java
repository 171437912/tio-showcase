/**
 *
 */
package org.tio.examples.im.server.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.tio.utils.thread.pool.DefaultThreadFactory;
import org.tio.utils.thread.pool.SynThreadPoolExecutor;

/**
 *
 * @author tanyaowu
 * 2017年7月7日 上午11:12:03
 */
public class Threads {
	public static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 1;

	// public static final int CORE_POOL_SIZE = _CORE_POOL_SIZE;// < 160 ? 160 :
	// _CORE_POOL_SIZE;

	public static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 4 < 16 ? 16 : CORE_POOL_SIZE * 4;

	public static final long KEEP_ALIVE_TIME = 90L;
	public static ThreadPoolExecutor groupExecutor = null;
	public static SynThreadPoolExecutor tioExecutor = null;

	static {
		LinkedBlockingQueue<Runnable> tioQueue = new LinkedBlockingQueue<>();
		String tioThreadName = "tio";
		tioExecutor = new SynThreadPoolExecutor(MAX_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, tioQueue, DefaultThreadFactory.getInstance(tioThreadName, Thread.NORM_PRIORITY),
				tioThreadName);
		tioExecutor.prestartAllCoreThreads();

		LinkedBlockingQueue<Runnable> groupQueue = new LinkedBlockingQueue<>();
		String groupThreadName = "tio-group";
		groupExecutor = new ThreadPoolExecutor(MAX_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, groupQueue,
				DefaultThreadFactory.getInstance(groupThreadName, Thread.NORM_PRIORITY));
		groupExecutor.prestartAllCoreThreads();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(Threads.groupExecutor);
	}

	/**
	 *
	 */
	public Threads() {
	}

}
