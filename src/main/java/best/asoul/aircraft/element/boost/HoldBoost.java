package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Boost;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.BoostType;
import best.asoul.aircraft.entity.HoldBoostState;
import best.asoul.aircraft.exception.AsoulException;

/**
 * @Description 玩家战机持有的增益效果
 * @Author Enchantedyou
 * @Date 2021年12月03日-21:02
 */
public abstract class HoldBoost extends Boost {

	/** 持有效果的类型 **/
	protected BoostType boostType;
	/** 当前状态 **/
	protected transient HoldBoostState boostState;

	protected HoldBoost(long duration) {
		super(GameContext.getStageDefine().getEffectList());
		this.boostState = new HoldBoostState(System.currentTimeMillis(), duration);
		final BoostType type = determineBoostType();
		if (null == type) {
			throw new AsoulException("增益效果类型不能为空");
		}
		this.boostType = type;
	}

	/**
	 * @Description 渲染动画
	 * @Author Enchantedyou
	 * @Date 2021/12/4-15:14
	 */
	public void renderAnimation() {
		// 追加动画
		final AnimationEffectPlayer selfEffect = determineSelfEffect();
		if (selfEffect != null) {
			selfEffectList.add(selfEffect);
			effectList.addAll(selfEffectList);
		}
	}

	/**
	 * @Description 指定或延长过期时间
	 * @Author Enchantedyou
	 * @Date 2021/12/3-21:07
	 * @param duration 延长毫秒数
	 */
	public synchronized void specifyOrProlongExpireTime(long duration) {
		boostState.setRemainDuration(boostState.getRemainDuration() + duration);
		if (boostState.getExpireTime() == 0L) {
			boostState.setExpireTime(System.currentTimeMillis() + duration);
		} else {
			boostState.setExpireTime(boostState.getExpireTime() + duration);
		}
	}

	/**
	 * @Description 判断当前持有的特效是否过期，如果过期，移除当前持有的动画效果
	 * @Author Enchantedyou
	 * @Date 2021/12/3-21:14
	 * @param aircraft
	 * @return boolean
	 */
	public boolean expiredAndRemove(Aircraft aircraft) {
		if (System.currentTimeMillis() >= boostState.getExpireTime()) {
			// 清除动画效果
			clearSelfEffect();
			// 特效过期后处理
			boostExpiredAfter(aircraft);
			return true;
		}
		return false;
	}

	/**
	 * @Description 保存当前特效状态
	 * @Author Enchantedyou
	 * @Date 2021/12/3-23:51
	 */
	public synchronized void saveState() {
		final long now = System.currentTimeMillis();
		// 剩余持续时间减少
		final long d = boostState.getRemainDuration() - (now - boostState.getStartTime());
		boostState.setRemainDuration(Math.max(d, 0L));
	}

	/**
	 * @Description 恢复特效状态
	 * @Author Enchantedyou
	 * @Date 2021/12/3-23:53
	 */
	public synchronized void recoveryState() {
		final long now = System.currentTimeMillis();
		// 重置开始时间和过期时间
		boostState.setStartTime(now);
		boostState.setExpireTime(now + boostState.getRemainDuration());
	}

	/**
	 * @Description 特效过期后处理
	 * @Author Enchantedyou
	 * @Date 2021/12/3-21:48
	 * @param aircraft
	 */
	public abstract void boostExpiredAfter(Aircraft aircraft);

	/**
	 * @Description 指定特效增益类型
	 * @Author Enchantedyou
	 * @Date 2021/12/3-22:04
	 * @return java.lang.String
	 */
	public abstract BoostType determineBoostType();

	public BoostType getBoostType() {
		return boostType;
	}
}
