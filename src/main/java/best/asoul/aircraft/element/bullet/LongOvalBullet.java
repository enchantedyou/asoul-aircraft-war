package best.asoul.aircraft.element.bullet;

import best.asoul.aircraft.config.bullet.EnemyBulletConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Bullet;

/**
 * @Description 长椭圆形子弹
 * @Author Enchantedyou
 * @Date 2021/11/27-23:29
 */
public class LongOvalBullet extends Bullet {

	public LongOvalBullet() {
		super(ResourceConst.LONG_OVAL_BULLET, new EnemyBulletConfig(20, 80));
		attack = 120;
	}

	@Override
	public void switchLevel(int bulletLevel, int awakeLevel) {
		throw new UnsupportedOperationException();
	}
}
