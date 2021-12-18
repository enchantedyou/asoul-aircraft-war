package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.config.boost.BoostConfig;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.factory.AnimationResourceFactory;
import best.asoul.aircraft.thread.base.AsoulThreadPoolHelper;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 游离态，向晚觉醒技：每600ms恢复1%生命值
 * @Author Enchantedyou
 * @Date 2021/12/4-13:33
 */
public class AvaDriftBoost extends DriftBoot {

	public AvaDriftBoost(Flying sourceFlying) {
		super(sourceFlying, new BoostConfig(92, 92));
	}

	@Override
	public void boostAfter(Aircraft aircraft) {
		final int awakeLevel = aircraft.awakeLevelUp();
		if (awakeLevel == 1) {
			// 一级觉醒，持续生命回复
			AsoulThreadPoolHelper.submitGameTask(() -> {
				while (!Thread.currentThread().isInterrupted() && !aircraft.isDead()) {
					aircraft.increaseHealthPointPercent(1);
					AsoulUtil.pause(500L);
				}
			});
		} else if (awakeLevel == 2) {
			// 二级觉醒：子弹攻击力翻倍，数量翻倍，散射
			aircraft.getBullet().increaseAttack(aircraft.getBullet().getAttack());
		} else {
			// 后续觉醒：子弹升级
			if (aircraft.bulletLevelUp() && duration > 0L) {
				// 如果暴走则添加持有效果
				aircraft.endowBoost(new EnergyRestoredHoldBoost(duration), duration);
			}
		}
	}

	@Override
	public long determineDuration() {
		return 4000L;
	}

	@Override
	protected AnimationEffectPlayer determineSelfEffect() {
		return AnimationResourceFactory.buildAnimationPlayer(AnimationType.AVA_AWAKE, this);
	}
}
