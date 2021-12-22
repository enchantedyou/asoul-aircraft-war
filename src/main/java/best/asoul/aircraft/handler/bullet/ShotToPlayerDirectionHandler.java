package best.asoul.aircraft.handler.bullet;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
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
		super(Arrays.asList(AircraftCamp.ENEMY, AircraftCamp.BOSS), turnCount, turnInterval);
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

			if (turnCount != 0 && --c <= 0) {
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
			AsoulUtil.enablePause();
			if (aircraft.isDead()) {
				return;
			}

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
		double degrees = AsoulUtil.calcEnemyShotDegrees(pMidX, pMidY, bMidX, bMidY);
		bullet.getConfig().setDegrees(degrees);
	}
}
