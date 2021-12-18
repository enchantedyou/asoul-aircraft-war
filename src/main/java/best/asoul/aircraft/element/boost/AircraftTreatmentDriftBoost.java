package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.config.boost.BoostConfig;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.factory.AnimationResourceFactory;

/**
 * @Description 游离态战机治疗效果
 * @Author Enchantedyou
 * @Date 2021/12/4-13:33
 */
public class AircraftTreatmentDriftBoost extends DriftBoot {

	public AircraftTreatmentDriftBoost(Flying sourceFlying) {
		super(sourceFlying, new BoostConfig(92, 92));

	}

	@Override
	public void boostAfter(Aircraft aircraft) {
		// 血量增加20%
		aircraft.increaseHealthPointPercent(20);
	}

	@Override
	public long determineDuration() {
		return 0L;
	}

	@Override
	protected AnimationEffectPlayer determineSelfEffect() {
		return AnimationResourceFactory.buildAnimationPlayer(AnimationType.AIRCRAFT_TREATMENT, this);
	}
}
