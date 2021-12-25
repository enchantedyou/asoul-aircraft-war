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
		super(ResourceConst.AVA_JELLYFISH_BULLET, new AsoulBulletConfig(45, 190));
		attack = 160;
	}

	@Override
	public void switchLevel(int bulletLevel, int awakeLevel) {
		if (bulletLevel == GlobalConst.BULLET_LEVEL_1) {
			setImageKey(ResourceConst.AVA_JELLYFISH_BULLET);
		} else if (bulletLevel == GlobalConst.BULLET_LEVEL_2) {
			if (awakeLevel >= 2) {
				// 散射
				setImageKey(ResourceConst.AVA_RAINDROPS_BULLET);
			} else {
				setImageKey(ResourceConst.AVA_JELLYFISH_BULLET);
			}
		} else if (bulletLevel >= GlobalConst.ENERGY_RESTORED_LEVEL) {
			setImageKey(ResourceConst.AVA_DRILL_BULLET);
		}
	}
}
