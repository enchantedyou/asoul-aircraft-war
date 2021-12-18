package best.asoul.aircraft.element.bullet;

import best.asoul.aircraft.config.bullet.AsoulBulletConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Bullet;

/**
 * @Description 向晚子弹
 * @Author Enchantedyou
 * @Date 2021/11/28-0:34
 */
public class AvaBullet extends Bullet {

	public AvaBullet() {
		super(ResourceConst.AVA_LEVEL1_BULLET, new AsoulBulletConfig(45, 190));
		attack = 280;
	}

	@Override
	public void switchLevel(int level) {
		if (level == GlobalConst.BULLET_LEVEL_1) {
			setImageKey(ResourceConst.AVA_LEVEL1_BULLET);
			flyingConfig.determineSize(45, 190);
		} else if (level == GlobalConst.BULLET_LEVEL_2) {
			setImageKey(ResourceConst.AVA_LEVEL2_BULLET);
			flyingConfig.determineSize(50, 180);
		} else if (level >= GlobalConst.ENERGY_RESTORED_LEVEL) {
			setImageKey(ResourceConst.AVA_LEVEL3_BULLET);
			flyingConfig.determineSize(60, 190);
		}
	}
}
