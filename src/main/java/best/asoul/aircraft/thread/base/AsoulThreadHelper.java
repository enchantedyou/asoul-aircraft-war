package best.asoul.aircraft.thread.base;

import best.asoul.aircraft.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description 任务线程池助手
 * @Author Enchantedyou
 * @Date 2021年11月21日-15:13
 */
@Slf4j
public class AsoulThreadHelper {

	private AsoulThreadHelper() {
	}

	/** 线程池 **/
	private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(
			GlobalConfig.CORE_THREAD_SIZE,
			GlobalConfig.MAX_THREAD_SIZE,
			GlobalConfig.KEEP_ALIVE_SECONDS,
			TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(GlobalConfig.QUEUE_THREAD_SIZE),
			new AsoulThreadFactory(true));

	/** 线程任务集合 **/
	protected static final List<Thread> TASK_LIST = new CopyOnWriteArrayList<>();
	/** 游戏暂停状态 **/
	private static AtomicReference<Boolean> gamePause = new AtomicReference<>(false);
	/** 暂停开始时间 **/
	private static AtomicLong pauseStartTime = new AtomicLong(0L);
	/** 暂停持续时间 **/
	private static AtomicLong pauseDuration = new AtomicLong(0L);
	/** 游戏准备计数器 **/
	private static CountDownLatch readyLatch = new CountDownLatch(1);

	/**
	 * @Description 获取线程池
	 * @Author Enchantedyou
	 * @Date 2021/11/21-15:28
	 * @return java.util.concurrent.ScheduledExecutorService
	 */
	public static ThreadPoolExecutor getThreadPool() {
		return THREAD_POOL;
	}

	/**
	 * @Description 获取线程池中的线程列表
	 * @Author Enchantedyou
	 * @Date 2021/11/29-21:08
	 * @return java.util.List<java.lang.Thread>
	 */
	public static List<Thread> getTaskList() {
		return TASK_LIST;
	}

	/**
	 * @Description 提交游戏线程
	 * @Author Enchantedyou
	 * @Date 2021/11/23-21:51
	 * @param task
	 */
	public static void submitTask(Runnable task) {
		THREAD_POOL.submit(task);
	}

	/**
	 * @Description 游戏暂停或恢复
	 * @Author Enchantedyou
	 * @Date 2021/11/23-21:53
	 */
	public static synchronized void gamePauseOrResume() {
		final Boolean pause = gamePause.get();
		for (Thread t : TASK_LIST) {
			if (pause != null && pause) {
				// 游戏恢复
				LockSupport.unpark(t);
				pauseDuration.getAndSet(System.currentTimeMillis() - pauseStartTime.get());
			} else {
				// 游戏暂停
				pauseStartTime.getAndSet(System.currentTimeMillis());
			}
		}

		// 更新动画状态
		Objects.requireNonNull(pause);
		gamePause.getAndSet(!pause);
	}

	/**
	 * @Description 获取并重置暂停持续时间
	 * @Author Enchantedyou
	 * @Date 2021/12/3-23:15
	 * @return long
	 */
	public static long getAndResetPauseDuration() {
		long v = pauseDuration.get();
		// 重置
		pauseDuration.getAndSet(0L);
		return v;
	}

	/**
	 * @Description 游戏是否暂停
	 * @Author Enchantedyou
	 * @Date 2021/12/22-22:21
	 * @return boolean
	 */
	public static boolean isGamePause() {
		return gamePause.get();
	}

	/**
	 * @Description 游戏准备完成之前进行等待
	 * @Author Enchantedyou
	 * @Date 2021/12/22-22:33
	 * @return boolean
	 */
	public static void readyAwait() {
		try {
			readyLatch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * @Description 完成准备
	 * @Author Enchantedyou
	 * @Date 2021/12/22-22:37
	 */
	public static void finishReady() {
		readyLatch.countDown();
	}
}
