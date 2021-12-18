package best.asoul.aircraft.handler.bullet;

import java.util.ArrayList;
import java.util.List;

import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 旋涡式散射
 * @Author Enchantedyou
 * @Date 2021年12月06日-21:40
 */
public class WhirlpoolSpreadShotHandler extends ShotHandler {

	/** 角度偏移量 **/
	private double degreesOffset;
	/** 是否等所有子弹初始完后再一起发射 **/
	private boolean shotTogether;
	/** 子弹移动速度 **/
	private int bulletSpeed;

	public WhirlpoolSpreadShotHandler(double degreesOffset,
			int turnCount,
			long turnInterval,
			boolean shotTogether,
			int bulletSpeed) {
		super(AircraftCamp.ENEMY, turnCount, turnInterval);
		if (turnCount <= 0 && shotTogether) {
			throw new IllegalArgumentException("齐射时发射轮数必须大于0");
		}

		this.degreesOffset = degreesOffset;
		this.shotTogether = shotTogether;
		this.bulletSpeed = bulletSpeed;
	}

	@Override
	public void shot(Aircraft aircraft) {
		int c = turnCount;
		List<Bullet> toRecoverySpeedBulletList = new ArrayList<>();
		double speed = bulletSpeed > 0 ? bulletSpeed : aircraft.getBullet().getConfig().getSpeed();
		while (!Thread.currentThread().isInterrupted() && !aircraft.isDead()) {
			// 每一轮随机一个旋涡中心
			int x = GlobalConst.RANDOM.nextInt(aircraft.getImage().getWidth()) + aircraft.getConfig().getX();
			int y = GlobalConst.RANDOM.nextInt(aircraft.getImage().getHeight()) + aircraft.getConfig().getY();

			for (double d = 0D; d < GlobalConst.DEGREES_OF_CIRCLE; d += degreesOffset) {
				final Bullet bullet = createBullet(aircraft);
				bullet.getConfig().setDegrees(d);
				bullet.moveTo(x, y);
				if (!shotTogether) {
					bullet.getConfig().setSpeed(speed);
				}

				toRecoverySpeedBulletList.add(bullet);
			}

			if (turnCount != 0 && c-- <= 0) {
				break;
			}
			AsoulUtil.pause(turnInterval);
		}

		if (shotTogether) {
			// 速度恢复
			for (Bullet bullet : toRecoverySpeedBulletList) {
				bullet.getConfig().setSpeed(speed);
			}
		}
		toRecoverySpeedBulletList.clear();
	}

	@Override
	protected void beforeAppendBullet(Bullet bullet) {
		if (shotTogether) {
			// 速度置零
			bullet.getConfig().setSpeed(0D);
		}
	}
}
