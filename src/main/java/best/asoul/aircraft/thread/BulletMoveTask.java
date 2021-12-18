package best.asoul.aircraft.thread;

import java.util.ArrayList;
import java.util.List;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.Quadrant;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.JFrameUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 子弹移动任务
 * @Author Enchantedyou
 * @Date 2021/11/21-15:49
 */
@Slf4j
public class BulletMoveTask implements Runnable {

	/** 子弹所属战机 **/
	private Aircraft aircraft;

	public BulletMoveTask(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			if (aircraft.isDead() && aircraft.getShotList().isEmpty()) {
				break;
			}

			// 子弹移动
			doBulletMove(aircraft);
			AsoulUtil.pause(aircraft.getBullet().getConfig().getMoveInterval());
		}
	}

	private void doBulletMove(Aircraft aircraft) {
		List<Bullet> removeList = new ArrayList<>();
		// 绘制刷新
		for (Bullet bullet : aircraft.getShotList()) {
			bullet.move();
			// 子弹越界检查
			final FlyingConfig bulletConfig = bullet.getConfig();
			final int x = bulletConfig.getX();
			final int y = bulletConfig.getY();
			// 上下越界
			if (y <= -bulletConfig.getHeight() || y >= GlobalConfig.SCREEN_HEIGHT) {
				removeList.add(bullet);
			}
			// 左右越界及反射处理
			doLeftRightReflect(removeList, bullet, bulletConfig, x);
		}
		// 移除越界子弹
		aircraft.getShotList().removeAll(removeList);
	}

	private void doLeftRightReflect(List<Bullet> removeList, Bullet bullet, FlyingConfig bulletConfig, int x) {
		final double curDegrees = bullet.getConfig().getDegrees();
		final Quadrant quadrant = JFrameUtil.getQuadrant(curDegrees);
		if (x <= -bulletConfig.getWidth() && (quadrant == Quadrant.TWO || quadrant == Quadrant.THREE)) {
			if (bullet.isLeftRightReflect()) {
				bullet.getConfig().setDegrees(curDegrees + GlobalConst.MAX_QUADRANT_DEGREES);
			} else {
				removeList.add(bullet);
			}
		} else if (x >= GlobalConfig.SCREEN_WIDTH - bulletConfig.getWidth()
				&& (quadrant == Quadrant.ONE || quadrant == Quadrant.FOUR)) {
			if (bullet.isLeftRightReflect()) {
				bullet.getConfig().setDegrees(curDegrees - GlobalConst.MAX_QUADRANT_DEGREES);
			} else {
				removeList.add(bullet);
			}
		}
	}
}
