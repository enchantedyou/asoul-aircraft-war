package best.asoul.aircraft.handler.bullet;

import java.awt.image.BufferedImage;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.Direction;
import best.asoul.aircraft.entity.Quadrant;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 朝玩家方向射击（不会追踪）
 * @Author Enchantedyou
 * @Date 2021年12月06日-20:50
 */
public class ShotToPlayerDirectionHandler extends ShotHandler {

	/** 发射子弹的数量 **/
	private int bulletCount;
	/** 每一发子弹的射击间隔 **/
	private long shotInterval;
	/** 子弹移动速度 **/
	private int bulletSpeed;

	public ShotToPlayerDirectionHandler(int bulletCount, long shotInterval, int bulletSpeed, int turnCount,
			long turnInterval) {
		super(AircraftCamp.ENEMY, turnCount, turnInterval);
		this.bulletCount = bulletCount;
		this.shotInterval = shotInterval;
		this.bulletSpeed = bulletSpeed;
	}

	public ShotToPlayerDirectionHandler(int bulletCount) {
		this(bulletCount, 0L, 0, 0, 0L);
	}

	@Override
	public void shot(Aircraft aircraft) {
		int c = turnCount;
		while (!Thread.currentThread().isInterrupted() && !aircraft.isDead()) {
			shotATurn(aircraft);

			if (c != 0 && c-- <= 0) {
				break;
			}
			AsoulUtil.pause(turnInterval);
		}
	}

	private void shotATurn(Aircraft aircraft) {
		if (bulletCount <= 0) {
			return;
		}
		for (int i = 0; i < bulletCount; i++) {
			final FlyingConfig bulletConfig = aircraft.getBullet().getConfig();
			final Bullet bullet = createBullet(aircraft);
			final BufferedImage bulletImage = bullet.getImage();
			if (bulletSpeed > 0) {
				bullet.getConfig().setSpeed(bulletSpeed);
			}
			// 计算射击角度
			calcShotDegrees(bullet, bulletImage);

			long pauseInterval = bulletConfig.getCreateInterval();
			if (shotInterval != 0L) {
				pauseInterval = shotInterval;
			}
			// 暂停
			AsoulUtil.pause(pauseInterval);
		}
	}

	private void calcShotDegrees(Bullet bullet, BufferedImage bulletImage) {
		// 计算玩家的中心点
		final Aircraft playerAircraft = GameContext.getPlayerAircraft();
		final FlyingConfig playerAircraftConfig = playerAircraft.getConfig();
		int pMidX = playerAircraftConfig.getX() + playerAircraft.getImage().getWidth() / 2;
		int pMidY = playerAircraftConfig.getY() + playerAircraft.getImage().getHeight() / 2;
		// 计算子弹的中心点
		int bMidX = bullet.getConfig().getX() + bulletImage.getWidth() / 2;
		int bMidY = bullet.getConfig().getY() + bulletImage.getHeight() / 2;
		// 计算两点之间的斜边距离、横向距离、纵向距离
		double hypotenuseDistance = AsoulUtil.calcDistanceBetweenPoints(pMidX, pMidY, bMidX, bMidY);
		double horizontalDistance = AsoulUtil.calcDistanceBetweenPoints(pMidX, 0, bMidX, 0);
		// 计算角度
		double degrees = Math.toDegrees(Math.asin(horizontalDistance / hypotenuseDistance));
		// 判定运动方向
		if (pMidX > bMidX) {
			// 第四象限（右下）
			bullet.getConfig().setDegrees(AsoulUtil.getActualDegrees(degrees, Quadrant.FOUR));
		} else if (pMidX < bMidX) {
			degrees = GlobalConst.MAX_QUADRANT_DEGREES - degrees;
			// 第四象限（左下）
			bullet.getConfig().setDegrees(AsoulUtil.getActualDegrees(degrees, Quadrant.THREE));
		} else {
			// 向下
			bullet.getConfig().setDegrees(Direction.DOWN.degrees());
		}
	}
}
