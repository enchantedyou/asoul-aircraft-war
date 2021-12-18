package best.asoul.aircraft.handler.bullet;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.invoker.Invoker;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 默认的玩家射击处理器
 * @Author Enchantedyou
 * @Date 2021年12月06日-21:02
 */
public class DefaultPlayerShotHandler extends ShotHandler {

	public DefaultPlayerShotHandler() {
		super(AircraftCamp.ASOUL, 0, 0L);
	}

	@Override
	public void shot(Aircraft aircraft) {
		while (!Thread.currentThread().isInterrupted() && !aircraft.isDead()) {
			final FlyingConfig bulletConfig = aircraft.getBullet().getConfig();
			final int bulletWidth = bulletConfig.getWidth();
			final int bulletHeight = bulletConfig.getHeight();

			if (aircraft.getBulletLevel() == GlobalConst.BULLET_LEVEL_1) {
				final Bullet b1 = createBullet(aircraft);
				final Bullet b2 = createBullet(aircraft);
				final Bullet b3 = createBullet(aircraft);
				b2.getConfig().setY(b2.getConfig().getY() - bulletHeight / 5);
				b1.getConfig().setX(b2.getConfig().getX() - bulletWidth / 2 - GlobalConst.BULLET_DISTANCE);
				b3.getConfig().setX(b2.getConfig().getX() + bulletWidth / 2 + GlobalConst.BULLET_DISTANCE);
			} else if (aircraft.getBulletLevel() >= GlobalConst.BULLET_LEVEL_2) {
				final Bullet b1 = createBullet(aircraft);
				final Bullet b2 = createBullet(aircraft);
				final Bullet b3 = createBullet(aircraft);
				final Bullet b4 = createBullet(aircraft);
				final Bullet b5 = createBullet(aircraft);
				b3.getConfig().setY(b3.getConfig().getY() - bulletHeight / 5);
				b2.getConfig().setX(b2.getConfig().getX() - bulletWidth / 3 - GlobalConst.BULLET_DISTANCE);
				b4.getConfig().setX(b4.getConfig().getX() + bulletWidth / 3 + GlobalConst.BULLET_DISTANCE);

				b1.getConfig().setX(b2.getConfig().getX() - bulletWidth / 2);
				b1.getConfig().setY(b2.getConfig().getY() + GlobalConst.BULLET_DISTANCE * 2);
				b5.getConfig().setX(b4.getConfig().getX() + bulletWidth / 2);
				b5.getConfig().setY(b4.getConfig().getY() + GlobalConst.BULLET_DISTANCE * 2);
			}

			// 暂停
			AsoulUtil.pause(bulletConfig.getCreateInterval());
		}
	}

	@Override
	public void setBeforeNextHandleInvoker(Invoker beforeNextHandleInvoker) {
		throw new UnsupportedOperationException();
	}
}
