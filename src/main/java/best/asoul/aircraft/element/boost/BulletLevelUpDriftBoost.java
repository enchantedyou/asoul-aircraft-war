package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.config.boost.BoostConfig;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.factory.AnimationResourceFactory;

/**
 * @Description 游离态子弹升级增强效果
 * @Author Enchantedyou
 * @Date 2021年11月28日-15:48
 */
public class BulletLevelUpDriftBoost extends DriftBoot {

	public BulletLevelUpDriftBoost(Flying sourceFlying) {
		super(sourceFlying, new BoostConfig(92, 92));
	}

	@Override
	public void boostAfter(Aircraft aircraft) {
		// 子弹升级
		if (aircraft.bulletLevelUp() && duration > 0L) {
			// 如果暴走则添加持有效果
			aircraft.endowBoost(new EnergyRestoredHoldBoost(duration), duration);
		}
	}

	@Override
	public long determineDuration() {
		return 4200L;
	}

	@Override
	protected AnimationEffectPlayer determineSelfEffect() {
		return AnimationResourceFactory.buildAnimationPlayer(AnimationType.BULLET_LEVEL_UP, this);
	}
}
