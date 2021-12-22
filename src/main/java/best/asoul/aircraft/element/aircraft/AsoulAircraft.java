package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.handler.bullet.DefaultPlayerShotHandler;
import best.asoul.aircraft.util.SoundUtil;

/**
 * @Description 一个魂儿战机！
 * @Author Enchantedyou
 * @Date 2021年11月20日-16:24
 */
public class AsoulAircraft extends Aircraft {

	public AsoulAircraft(String imageKey, Bullet bullet) {
		super(imageKey, bullet);
		initHealthPoint(5000);
		super.camp = AircraftCamp.ASOUL;
		flyingConfig.determineSize(30, 30);
	}

	@Override
	public boolean bulletLevelUp() {
		int level = bulletLevel.incrementAndGet();
		bullet.switchLevel(level, getAwakeLevel());
		if (level >= GlobalConst.ENERGY_RESTORED_LEVEL) {
			triggerEnergyRestored();
			// 如果子弹升级后刚好到达暴走等级，说明是从未暴走状态切换为暴走状态，否则为延续暴走状态，不再重复增强子弹
			if (level == GlobalConst.ENERGY_RESTORED_LEVEL) {
				bulletBoost();
			}
			return true;
		}
		return false;
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		shotChain.append(new DefaultPlayerShotHandler());
	}

	@Override
	public void afterDead() {
		SoundUtil.playAvaRambo();
	}

	private void triggerEnergyRestored() {
		// 触发暴走
		bulletLevel.getAndSet(GlobalConst.ENERGY_RESTORED_LEVEL);
	}
}
