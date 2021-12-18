package best.asoul.aircraft.element.boost;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Boost;
import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.factory.AnimationResourceFactory;

/**
 * @Description 游离态增强效果（在屏幕上飘来飘去的那种）
 * @Author Enchantedyou
 * @Date 2021年12月01日-20:25
 */
public abstract class DriftBoot extends Boost {

	/** 当前增强效果是否被使用 **/
	protected boolean used = false;
	/** 指定当前效果的持续时间 **/
	protected long duration;

	protected DriftBoot(Flying sourceFlying, FlyingConfig boostConfig) {
		super(GameContext.getStageDefine().getEffectList());
		super.flyingConfig = boostConfig;
		// 初始化加载
		initLoad(sourceFlying);
	}

	private void initLoad(Flying sourceFlying) {
		final AnimationEffectPlayer boostHalo = AnimationResourceFactory.buildAnimationPlayer(AnimationType.BOOST_HALO,
				this);
		// 指定当前效果的持续时长
		this.duration = determineDuration();
		// 取外圈第一帧作为增强效果的图像
		image = boostHalo.getImageList().get(0);
		// 追加内圈动画
		final AnimationEffectPlayer innerCircleEffect = determineSelfEffect();
		if (innerCircleEffect != null) {
			selfEffectList.add(innerCircleEffect);
			// 追加外圈动画
			selfEffectList.add(boostHalo);
		}
		// 开始动画渲染
		effectList.addAll(selfEffectList);
		// 随机角度重新设置X、Y轴移动速度
		resetMoveDegrees();
		// 指定增益效果生成的位置
		determineCreatePosition(sourceFlying.getConfig());
	}

	/**
	 * @param sourceFlyingConfig
	 * @Description 指定增益效果生成的位置
	 * @Author Enchantedyou
	 * @Date 2021/12/1-22:21
	 */
	protected void determineCreatePosition(FlyingConfig sourceFlyingConfig) {
		flyingConfig.determinePosition(sourceFlyingConfig.getX(), sourceFlyingConfig.getY());
		// 如果在屏幕上方界外生成，则按照原飞行物的判定高度往下挪
		if (flyingConfig.getX() >= 0 && flyingConfig.getY() <= 0) {
			flyingConfig.setY(flyingConfig.getY() + sourceFlyingConfig.getHeight() + GlobalConst.BOOST_OFFSET_Y);
		}
	}

	/**
	 * @return boolean
	 * @Description 判断当前buff是否被使用
	 * @Author Enchantedyou
	 * @Date 2021/12/1-20:25
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * @param aircraft
	 * @Description 特效触发
	 * @Author Enchantedyou
	 * @Date 2021/12/1-20:51
	 */
	public final void boostTrigger(Aircraft aircraft) {
		if (used) {
			return;
		}
		used = true;
		boostAfter(aircraft);
		clearSelfEffect();
	}

	/**
	 * @Description 重置运动角度
	 * @Author Enchantedyou
	 * @Date 2021/11/28-20:14
	 */
	protected void resetMoveDegrees() {
		// 随机变更角度
		flyingConfig.setDegrees(GlobalConst.RANDOM.nextInt(360));
		move();
	}

	/**
	 * @param aircraft 被增强的战机
	 * @Description 增强后处理
	 * @Author Enchantedyou
	 * @Date 2021/11/28-16:14
	 */
	public abstract void boostAfter(Aircraft aircraft);

	/**
	 * @return long
	 * @Description 指定当前增益效果的持续时长
	 * @Author Enchantedyou
	 * @Date 2021/12/3-21:16
	 */
	public abstract long determineDuration();
}
