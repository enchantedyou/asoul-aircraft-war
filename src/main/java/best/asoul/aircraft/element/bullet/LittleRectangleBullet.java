package best.asoul.aircraft.element.bullet;

import best.asoul.aircraft.config.bullet.EnemyBulletConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Bullet;

/**
 * @Description 小矩形子弹
 * @Author Enchantedyou
 * @Date 2021/11/27-23:28
 */
public class LittleRectangleBullet extends Bullet {

	public LittleRectangleBullet() {
		super(ResourceConst.LITTLE_RECTANGLE_BULLET, new EnemyBulletConfig(30, 30));
		attack = 150;
	}

	@Override
	public void switchLevel(int bulletLevel, int awakeLevel) {
		throw new UnsupportedOperationException();
	}
}
