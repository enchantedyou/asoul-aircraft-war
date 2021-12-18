package best.asoul.aircraft.element.base;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import best.asoul.aircraft.entity.AnimationEffectPlayer;

/**
 * @Description 抽象的增强效果类
 * @Author Enchantedyou
 * @Date 2021年11月28日-16:05
 */
public abstract class Boost extends Flying implements Serializable {

	/** 自身效果列表 **/
	protected transient List<AnimationEffectPlayer> selfEffectList = new CopyOnWriteArrayList<>();
	/** 当前画面所有效果列表 **/
	protected transient List<AnimationEffectPlayer> effectList;

	protected Boost(List<AnimationEffectPlayer> effectList) {
		this.effectList = effectList;
	}

	/**
	 * @Description 清除自身动画效果
	 * @Author Enchantedyou
	 * @Date 2021/12/1-20:30
	 */
	protected void clearSelfEffect() {
		if (effectList != null) {
			effectList.removeAll(selfEffectList);
		}
		selfEffectList.clear();
	}

	/**
	 * @Description 指定当前增益效果的内圈动画
	 * @Author Enchantedyou
	 * @Date 2021/12/1-20:49
	 * @return best.asoul.aircraft.entity.AnimationEffectPlayer
	 */
	protected abstract AnimationEffectPlayer determineSelfEffect();
}
