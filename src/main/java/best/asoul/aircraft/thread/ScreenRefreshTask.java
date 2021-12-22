package best.asoul.aircraft.thread;

import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.*;

import best.asoul.aircraft.config.UserConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.SoundUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 画面刷新的任务
 * @Author Enchantedyou
 * @Date 2021年11月22日-20:20
 */
@Slf4j
public class ScreenRefreshTask implements Runnable {

	/** 需要被刷新的面板 **/
	private JPanel jPanel;

	public ScreenRefreshTask(JPanel jPanel) {
		this.jPanel = jPanel;
	}

	@Override
	public void run() {
		// 提交游戏状态监控线程
		AsoulThreadHelper.submitTask(new GameReadyTask());

		// 每隔5秒输出一次线程池状态
		final long threadPoolMonitorInterval = 5000L;
		long t = System.currentTimeMillis();
		final ThreadPoolExecutor threadPool = AsoulThreadHelper.getThreadPool();

		// 计算停顿间隔
		final long interval = calcInterval(UserConfig.FRAME_RATE);
		while (!Thread.currentThread().isInterrupted()) {
			AsoulUtil.enablePause();
			jPanel.repaint();
			AsoulUtil.pause(interval);

			final long now = System.currentTimeMillis();
			if (now - t >= threadPoolMonitorInterval) {
				t = now;
				log.info("活跃线程数：{}，线程池大小：{}，历史最大线程数：{}", threadPool.getActiveCount(), threadPool.getPoolSize(),
						threadPool.getLargestPoolSize());
				// 定期清理已完成的核心线程数外的线程
				AsoulThreadHelper.getTaskList().removeIf(thread -> thread.getState() == Thread.State.TERMINATED);
				// 定期清理失效的声音切片
				log.info("清理失效的音效资源数：{}", SoundUtil.clearInvalidClip());
			}
		}
	}

	private long calcInterval(int frameRate) {
		if (frameRate > GlobalConst.MAX_FRAME_RATE) {
			frameRate = GlobalConst.MAX_FRAME_RATE;
		} else if (frameRate < GlobalConst.MIN_FRAME_RATE) {
			frameRate = GlobalConst.MIN_FRAME_RATE;
		}
		return 1000L / frameRate;
	}
}
