package best.asoul.aircraft.element.bullet;

import best.asoul.aircraft.config.bullet.EnemyBulletConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Bullet;

/**
 * @Description 小球型子弹
 * @Author Enchantedyou
 * @Date 2021/11/27-23:28
 */
public class LittleBallBullet extends Bullet {

	public LittleBallBullet() {
		super(ResourceConst.LITTLE_BALL_BULLET, new EnemyBulletConfig(25, 25));
		attack = 80;
	}

	@Override
	public void switchLevel(int level) {
		throw new UnsupportedOperationException();
	}
}
