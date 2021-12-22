package best.asoul.aircraft.element.bullet;

import best.asoul.aircraft.config.bullet.EnemyBulletConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Bullet;

/**
 * @Description boss波形子弹
 * @Author Enchantedyou
 * @Date 2021/12/19-21:54
 */
public class BossWaveBullet extends Bullet {

	public BossWaveBullet() {
		super(ResourceConst.BOSS_WAVE_BULLET, new EnemyBulletConfig(135, 160));
		attack = 750;
	}

	@Override
	public void switchLevel(int bulletLevel, int awakeLevel) {
		throw new UnsupportedOperationException();
	}
}
