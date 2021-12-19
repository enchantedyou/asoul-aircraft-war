package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.entity.BoostType;
import best.asoul.aircraft.factory.AnimationResourceFactory;

/**
 * @Description 伤害免疫效果
 * @Author Enchantedyou
 * @Date 2021/12/4-13:39
 */
public class AbsoluteDefenseHoldBoost extends HoldBoost {

	protected AbsoluteDefenseHoldBoost(long duration) {
		super(duration);
	}

	@Override
	public void boostExpiredAfter(Aircraft aircraft) {
		// 清除敌方所有子弹
		for (Aircraft enemy : GameContext.getStageDefine().getEnemyList()) {
			clearBullet(enemy);
		}
		for (Aircraft enemy : GameContext.getStageDefine().getToBeRemovedAircraftList()) {
			clearBullet(enemy);
		}
	}

	private void clearBullet(Aircraft enemy) {
		if (enemy.getCamp() != AircraftCamp.ASOUL) {
			enemy.getShotList().clear();
		}
	}

	@Override
	public BoostType determineBoostType() {
		return BoostType.ABSOLUTE_DEFENSE;
	}

	@Override
	protected AnimationEffectPlayer determineSelfEffect() {
		return AnimationResourceFactory.buildAnimationPlayer(AnimationType.SHIELD_PROTECT,
				GameContext.getPlayerAircraft());
	}
}
