package best.asoul.aircraft.handler.bullet;

import java.util.Objects;

import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 附带反射的多角度直线射击
 * @Author Enchantedyou
 * @Date 2021年12月08日-21:02
 */
public class StraightWithReflectHandler extends ShotHandler {

	/** 角度选项 **/
	private Double[] degreesOptions;
	/** 子弹速度 **/
	private int bulletSpeed;
	/** 射击间隔 **/
	private long shotInterval;
	/** 持续时间 **/
	private long duration;
	/** 是否反射 **/
	private boolean leftRightReflect;

	public StraightWithReflectHandler(boolean leftRightReflect, int bulletSpeed, long duration, long shotInterval,
			Double... degreesOptions) {
		super(AircraftCamp.ENEMY, 0, 0L);
		this.degreesOptions = Objects.requireNonNull(degreesOptions);
		this.bulletSpeed = bulletSpeed;
		this.shotInterval = shotInterval;
		this.duration = duration;
		this.leftRightReflect = leftRightReflect;
	}

	@Override
	public void shot(Aircraft aircraft) {
		long startTime = System.currentTimeMillis();
		while (!Thread.currentThread().isInterrupted() && !aircraft.isDead()) {
			for (double degrees : degreesOptions) {
				final Bullet bullet = createBullet(aircraft);
				bullet.getConfig().setDegrees(degrees);
				bullet.setLeftRightReflect(leftRightReflect);

				if (bulletSpeed > 0) {
					bullet.getConfig().setSpeed(bulletSpeed);
				}
			}

			// 持续时间限制
			if (duration != 0L && System.currentTimeMillis() - startTime > duration) {
				break;
			}

			if (shotInterval > 0L) {
				AsoulUtil.pause(shotInterval);
			} else {
				AsoulUtil.pause(aircraft.getBullet().getConfig().getCreateInterval());
			}
		}
	}
}
