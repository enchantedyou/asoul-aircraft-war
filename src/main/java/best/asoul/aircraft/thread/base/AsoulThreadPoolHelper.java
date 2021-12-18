package best.asoul.aircraft.thread.base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.thread.ScreenRefreshTask;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 任务线程池助手
 * @Author Enchantedyou
 * @Date 2021年11月21日-15:13
 */
@Slf4j
public class AsoulThreadPoolHelper {

	private AsoulThreadPoolHelper() {
	}

	/** 飞机创建（仅敌机）、子弹创建移动的线程池 **/
	private static final ThreadPoolExecutor GAME_THREAD_POOL = new ThreadPoolExecutor(
			GlobalConfig.CORE_THREAD_SIZE,
			GlobalConfig.MAX_THREAD_SIZE,
			GlobalConfig.KEEP_ALIVE_SECONDS,
			TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(GlobalConfig.QUEUE_THREAD_SIZE),
			new AsoulThreadFactory(true));
	/** 游戏监控线程池，这个线程池里面的任务不会被暂停 **/
	private static final ThreadPoolExecutor NO_PAUSE_THREAD_POOL = new ThreadPoolExecutor(
			4, 8, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(4),
			new AsoulThreadFactory(false));

	/** 线程任务集合 **/
	protected static final List<Thread> TASK_LIST = new CopyOnWriteArrayList<>();
	/** 被暂停的线程集合 **/
	private static final List<Thread> PAUSED_TASK_LIST = new CopyOnWriteArrayList<>();
	/** 游戏暂停状态 **/
	private static volatile boolean gamePause = false;
	/** 游戏状态更新的锁 **/
	private static final Object LOCK = new Object();
	/** 暂停开始时间 **/
	private static AtomicLong pauseStartTime = new AtomicLong(0L);
	/** 暂停持续时间 **/
	private static AtomicLong pauseDuration = new AtomicLong(0L);

	/**
	 * @Description 获取线程池
	 * @Author Enchantedyou
	 * @Date 2021/11/21-15:28
	 * @return java.util.concurrent.ScheduledExecutorService
	 */
	public static ThreadPoolExecutor getGameThreadPool() {
		return GAME_THREAD_POOL;
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
	 * @Description 提交游戏线程（飞机创建；子弹创建、移动；奖励创建、移动）
	 * @Author Enchantedyou
	 * @Date 2021/11/23-21:51
	 * @param task
	 */
	public static void submitGameTask(Runnable task) {
		if (task instanceof ScreenRefreshTask) {
			throw new AsoulException("禁止在游戏相关线程池中创建动画刷新线程");
		}
		GAME_THREAD_POOL.submit(task);
	}

	/**
	 * @Description 提交非暂停线程：动画刷新、游戏状态控制
	 * @Author Enchantedyou
	 * @Date 2021/12/12-20:47
	 * @param task
	 */
	public static void submitNoPauseTask(Runnable task) {
		NO_PAUSE_THREAD_POOL.submit(task);
	}

	/**
	 * @Description 游戏暂停或恢复
	 * @Author Enchantedyou
	 * @Date 2021/11/23-21:53
	 */
	public static void gamePauseOrResume() {
		synchronized (LOCK) {
			for (Thread task : TASK_LIST) {
				if (gamePause) {
					if (PAUSED_TASK_LIST.contains(task)) {
						// 游戏恢复
						task.resume();
						pauseDuration.getAndSet(System.currentTimeMillis() - pauseStartTime.get());
					}
				} else if (task.getState() == Thread.State.RUNNABLE || task.getState() == Thread.State.TIMED_WAITING) {
					// 游戏暂停
					task.suspend();
					PAUSED_TASK_LIST.add(task);
					pauseStartTime.getAndSet(System.currentTimeMillis());
				}
			}

			if (gamePause) {
				PAUSED_TASK_LIST.clear();
			}
			// 更新动画状态
			gamePause = !gamePause;
		}
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
}
