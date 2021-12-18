package best.asoul.aircraft.thread;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.Direction;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 敌机移动
 * @Author Enchantedyou
 * @Date 2021/11/21-17:38
 */
public class EnemyAircraftMoveTask implements Runnable {

	/** 战机 **/
	private Aircraft aircraft;

	public EnemyAircraftMoveTask(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public void run() {
		// 战机移动
		while (!Thread.currentThread().isInterrupted()) {
			// 如果战机阵亡则结束当前战机的移动线程
			if (aircraft.isDead()) {
				break;
			}

			final FlyingConfig aircraftConfig = aircraft.getConfig();
			final int offsetY = aircraft.getImage().getHeight() + aircraftConfig.getY();
			final boolean allowMove = offsetY <= aircraftConfig.getMaxEnemyY()
					|| aircraftConfig.getDegrees() != Direction.DOWN.degrees();
			if (aircraft.getCamp() == AircraftCamp.ENEMY && allowMove) {
				aircraft.move();
			}
			AsoulUtil.pause(aircraftConfig.getMoveInterval());
		}
	}
}
