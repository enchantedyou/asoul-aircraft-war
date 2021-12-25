package best.asoul.aircraft.element.bullet;

import best.asoul.aircraft.config.bullet.EnemyBulletConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Bullet;

/**
 * @Description 骷髅子弹
 * @Author Enchantedyou
 * @Date 2021/12/24-21:48
 */
public class SkeletonBullet extends Bullet {

	public SkeletonBullet() {
		super(ResourceConst.SKELETON_BULLET, new EnemyBulletConfig(70, 220));
		attack = 300;
	}

	@Override
	public void switchLevel(int bulletLevel, int awakeLevel) {
		throw new UnsupportedOperationException();
	}
}
