package best.asoul.aircraft.thread;

import java.util.List;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.config.boost.BoostConfig;
import best.asoul.aircraft.element.base.Boost;
import best.asoul.aircraft.element.boost.DriftBoot;
import best.asoul.aircraft.util.AsoulUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 增强效果移动任务（所有增益效果的移动速度都是一样的，一个线程就够了）
 * @Author Enchantedyou
 * @Date 2021/11/28-16:25
 */
@Slf4j
public class BoostMoveTask implements Runnable {

	/** 游离的增强效果列表 **/
	private List<DriftBoot> driftBootList;

	public BoostMoveTask(List<DriftBoot> driftBootList) {
		this.driftBootList = driftBootList;
	}

	@Override
	public void run() {
		final int moveInterval = new BoostConfig(0, 0).getMoveInterval();
		while (!Thread.currentThread().isInterrupted()) {
			for (DriftBoot boost : driftBootList) {
				if (boost.isUsed()) {
					driftBootList.remove(boost);
					continue;
				}
				// 碰壁反弹
				rebound(boost);
			}
			AsoulUtil.pause(moveInterval);
		}
	}

	private void rebound(Boost boost) {
		final FlyingConfig boostConfig = boost.getConfig();
		/** 四个方向碰壁校验开始 **/
		if (boostConfig.getY() <= 0
				|| (boostConfig.getY() + boostConfig.getHeight()) >= GlobalConfig.SCREEN_HEIGHT) {
			// 碰到上下边，X不变，Y反方向
			boost.yReverse();
		} else if (boostConfig.getX() <= 0
				|| (boostConfig.getX() + boostConfig.getWidth()) >= GlobalConfig.SCREEN_WIDTH) {
			// 碰到左右，Y不变，X反方向
			boost.xReverse();
		}
		boost.move();
		/** 四个方向碰壁校验结束 **/
	}
}
