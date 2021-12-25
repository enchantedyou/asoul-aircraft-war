package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.config.boost.BoostConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.factory.AnimationResourceFactory;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.SoundUtil;

/**
 * @Description 游离态，向晚觉醒技：每300ms恢复1%生命值
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
			SoundUtil.playSlowHeal();
			// 一级觉醒，持续生命回复
			AsoulThreadHelper.submitTask(() -> {
				while (!Thread.currentThread().isInterrupted() && !aircraft.isDead()) {
					aircraft.increaseHealthPointPercent(1);
					AsoulUtil.pause(300L);
				}
			});
		} else if (awakeLevel == 2) {
			SoundUtil.playPlayerAwake();
			// 二级觉醒：子弹攻击力提升50%，数量翻倍，散射
			aircraft.getBullet().increaseAttack(aircraft.getBullet().getAttack() / 2);
			// 切换为雨点子弹
			aircraft.getBullet().setImageKey(ResourceConst.AVA_RAINDROPS_BULLET);
		} else {
			// 后续觉醒：子弹升级
			if (aircraft.bulletLevelUp() && duration > 0L) {
				// 如果已暴走则追加暴走时长
				aircraft.endowBoost(new EnergyRestoredHoldBoost(duration), duration);
			}
			SoundUtil.playPlayerAwake();
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
