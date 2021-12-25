package best.asoul.aircraft.thread;

import java.awt.image.BufferedImage;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.Direction;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 敌机移动
 * @Author Enchantedyou
 * @Date 2021/11/21-17:38
 */
public class EnemyAircraftMoveTask implements Runnable {

	/** 战机 **/
	private Aircraft aircraft;
	/** X轴最大偏移量 **/
	private static final int MAX_X_OFFSET = 600;
	/** 偏移量每次变更值 **/
	private static final int ADJUST_X_OFFSET = 200;
	/** 当前偏移量 **/
	private int xOffset = MAX_X_OFFSET;
	/** 偏移量是否降序 **/
	private boolean descOffset = true;

	public EnemyAircraftMoveTask(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public void run() {
		AsoulThreadHelper.readyAwait();
		// 战机移动
		while (!Thread.currentThread().isInterrupted() && !aircraft.isDead()) {
			AsoulUtil.enablePause();

			final FlyingConfig aircraftConfig = aircraft.getConfig();
			final BufferedImage image = aircraft.getImage();
			final int offsetY = image.getHeight() + aircraftConfig.getY();
			// boss特定移动规则
			if (aircraft.getCamp() == AircraftCamp.BOSS) {
				if (offsetY > aircraftConfig.getMaxEnemyY()) {
					// 左右平移
					panLeftAndRight(aircraftConfig, image);
				}
				aircraft.move();
			} else {
				// 常规移动
				routineEnemyMove(aircraftConfig, offsetY);
			}
			AsoulUtil.pause(aircraftConfig.getMoveInterval());
		}
	}

	private void panLeftAndRight(FlyingConfig aircraftConfig, BufferedImage image) {
		aircraftConfig.setSpeed(2D);
		if (aircraftConfig.getDegrees() == Direction.DOWN.degrees()) {
			aircraftConfig.setDegrees(Direction.RIGHT.degrees());
		}
		if (aircraftConfig.getX() > GlobalConfig.SCREEN_WIDTH - image.getWidth() - xOffset) {
			aircraftConfig.setDegrees(Direction.LEFT.degrees());
		}
		if (aircraftConfig.getDegrees() == Direction.LEFT.degrees() && aircraftConfig.getX() < xOffset) {
			// 每经历一个左右移动的周期，偏移量做相应调整
			if (xOffset >= MAX_X_OFFSET) {
				descOffset = true;
			} else if (xOffset <= 0) {
				descOffset = false;
			}

			if (descOffset) {
				xOffset -= ADJUST_X_OFFSET;
			} else {
				xOffset += ADJUST_X_OFFSET;
			}
			aircraftConfig.setDegrees(Direction.RIGHT.degrees());
		}
	}

	private void routineEnemyMove(FlyingConfig aircraftConfig, int offsetY) {
		final boolean allowMove = offsetY <= aircraftConfig.getMaxEnemyY()
				|| aircraftConfig.getDegrees() != Direction.DOWN.degrees();
		if (aircraft.getCamp() != AircraftCamp.ASOUL && allowMove) {
			aircraft.move();
		}
	}
}
