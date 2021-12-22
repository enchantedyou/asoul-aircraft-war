package best.asoul.aircraft.element.bullet;

import best.asoul.aircraft.config.bullet.EnemyBulletConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Bullet;

/**
 * @Description 中等弧形子弹
 * @Author Enchantedyou
 * @Date 2021/11/27-23:31
 */
public class MidArcBullet extends Bullet {

	public MidArcBullet() {
		super(ResourceConst.MID_ARC_BULLET, new EnemyBulletConfig(25, 60));
		attack = 130;
	}

	@Override
	public void switchLevel(int bulletLevel, int awakeLevel) {
		throw new UnsupportedOperationException();
	}
}
