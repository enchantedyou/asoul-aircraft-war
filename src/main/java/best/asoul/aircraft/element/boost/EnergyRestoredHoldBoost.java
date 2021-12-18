package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.BoostType;

/**
 * @Description 暴走效果
 * @Author Enchantedyou
 * @Date 2021年12月03日-21:34
 */
public class EnergyRestoredHoldBoost extends HoldBoost {

	protected EnergyRestoredHoldBoost(long duration) {
		super(duration);
	}

	@Override
	public void boostExpiredAfter(Aircraft aircraft) {
		aircraft.endEnergyRestored();
	}

	@Override
	public BoostType determineBoostType() {
		return BoostType.BULLET_LEVEL_UP;
	}

	@Override
	protected AnimationEffectPlayer determineSelfEffect() {
		// 暴走没动画效果
		return null;
	}
}
