package best.asoul.aircraft.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.util.JFrameUtil;

/**
 * @Description 动画帧播放器
 * @Author Enchantedyou
 * @Date 2021年11月27日-16:36
 */
public class AnimationEffectPlayer implements Serializable {

	/** 动画类型 **/
	private transient List<BufferedImage> imageList;
	/** 动画类型 **/
	private AnimationType animationType;
	/** 飞行物 **/
	private Flying flying;
	/** 当前播放了几次 **/
	private int currentPlayTimes = 0;
	/** 当前播放到第几帧 **/
	private int currentFrameIndex = 0;
	/** 当前旋转角度 **/
	private double currentSpinDegrees = 0D;

	public AnimationEffectPlayer(AnimationType animationType, Flying flying) {
		this.flying = flying;
		this.animationType = animationType;
	}

	/**
	 * @Description 判断动画效果是否播放完成
	 * @Author Enchantedyou
	 * @Date 2021/11/27-16:48
	 * @return boolean
	 */
	public boolean isPlayFinish() {
		if (null == imageList || imageList.isEmpty()) {
			return true;
		} else if (animationType.isLoop()) {
			return false;
		}
		return isPlayFinishCycle();
	}

	/**
	 * @Description 是否播放完一个周期
	 * @Author Enchantedyou
	 * @Date 2021/11/28-13:32
	 * @return boolean
	 */
	private boolean isPlayFinishCycle() {
		return (currentPlayTimes == animationType.getPlayTimesPerFrame())
				&& (currentFrameIndex == imageList.size() - 1);
	}

	/**
	 * @Description 播放一帧动画
	 * @Author Enchantedyou
	 * @Date 2021/11/27-17:35
	 * @param g 画笔
	 */
	public void play(Graphics g) {
		if (null == imageList || imageList.isEmpty()) {
			return;
		}
		if (animationType.isLoop()) {
			if (isPlayFinishCycle()) {
				currentFrameIndex = 0;
				currentPlayTimes = 0;
			}
		} else if (isPlayFinish()) {
			return;
		}
		if (animationType.getPlayTimesPerFrame() == 0) {
			currentFrameIndex++;
		} else if (++currentPlayTimes > animationType.getPlayTimesPerFrame()) {
			currentFrameIndex++;
			currentPlayTimes = 1;
		}
		BufferedImage image = imageList.get(currentFrameIndex);
		final FlyingConfig flyingConfig = flying.getConfig();

		int flyingWidth = flying.getImage().getWidth();
		int flyingHeight = flying.getImage().getHeight();
		if (0 == flyingWidth || 0 == flyingHeight) {
			flyingWidth = flyingConfig.getWidth();
			flyingHeight = flyingConfig.getHeight();
		}

		// 计算飞行物的中心点
		int flyingMidX = flyingConfig.getX() + (flyingWidth / 2);
		int flyingMidY = flyingConfig.getY() + (flyingHeight / 2);
		// 计算动画的起始点
		int x = flyingMidX - (image.getWidth() / 2);
		int y = flyingMidY - (image.getHeight() / 2);

		// 旋转，每帧旋转10°
		if (animationType.isLoop() && animationType.getFrameCount() == 1 && animationType.getPlayTimesPerFrame() == 1) {
			currentSpinDegrees += 5D;
			if (currentSpinDegrees >= GlobalConst.DEGREES_OF_CIRCLE) {
				currentSpinDegrees = JFrameUtil.getFixedDegrees(currentSpinDegrees);
			}
			image = JFrameUtil.rotateImage(image, currentSpinDegrees);
		}
		g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
	}

	public List<BufferedImage> getImageList() {
		return imageList;
	}

	public void setImageList(List<BufferedImage> imageList) {
		this.imageList = imageList;
	}
}
