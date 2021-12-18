package best.asoul.aircraft.thread;

import best.asoul.aircraft.thread.base.AsoulThreadPoolHelper;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.SoundUtil;

/**
 * @Description 游戏监控任务
 * @Author Enchantedyou
 * @Date 2021年12月12日-20:49
 */
public class GameMonitorTask implements Runnable {

	@Override
	public void run() {
		// 游戏线程暂停2秒钟
		AsoulThreadPoolHelper.gamePauseOrResume();
		SoundUtil.playGameReady();
		AsoulUtil.pause(2000L);
		SoundUtil.playAvaPushStream();
		AsoulThreadPoolHelper.gamePauseOrResume();

		// 开始游戏状态监控
	}
}
