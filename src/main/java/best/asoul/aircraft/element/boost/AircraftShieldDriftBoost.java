package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.config.boost.BoostConfig;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.factory.AnimationResourceFactory;

/**
 * @Description 游离态护盾效果
 * @Author Enchantedyou
 * @Date 2021/12/4-13:34
 */
public class AircraftShieldDriftBoost extends DriftBoot {

	public AircraftShieldDriftBoost(Flying sourceFlying) {
		super(sourceFlying, new BoostConfig(92, 92));

	}

	@Override
	public void boostAfter(Aircraft aircraft) {
		aircraft.endowBoost(new AbsoluteDefenseHoldBoost(duration), duration);
	}

	@Override
	public long determineDuration() {
		return 4000L;
	}

	@Override
	protected AnimationEffectPlayer determineSelfEffect() {
		return AnimationResourceFactory.buildAnimationPlayer(AnimationType.AIRCRAFT_SHIELD, this);
	}
}
