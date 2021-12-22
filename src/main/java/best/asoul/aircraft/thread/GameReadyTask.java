package best.asoul.aircraft.thread;

import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.SoundUtil;

/**
 * @Description 游戏准备
 * @Author Enchantedyou
 * @Date 2021年12月12日-20:49
 */
public class GameReadyTask implements Runnable {

	@Override
	public void run() {
		// 游戏线程暂停2秒钟
		SoundUtil.playGameReady();
		AsoulUtil.pause(2000L);
		SoundUtil.playAvaPushStream();
		// 计数完成，其他线程开始工作
		AsoulThreadHelper.finishReady();
	}
}
