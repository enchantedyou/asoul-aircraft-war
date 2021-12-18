package best.asoul.aircraft.element.base;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.factory.ImageResourceFactory;

/**
 * @Description 飞行物
 * @Author Enchantedyou
 * @Date 2021年11月20日-00:08
 */
public abstract class Flying implements Serializable {

	protected String imageKey;
	/** 图片 **/
	protected transient BufferedImage image;
	/** 飞行物基础配置 **/
	protected FlyingConfig flyingConfig;
	/** x轴运动方向反转 **/
	protected boolean xReverse = false;
	/** y轴运动方向反转 **/
	protected boolean yReverse = false;

	protected Flying(String imageKey, FlyingConfig flyingConfig) {
		this.imageKey = imageKey;
		this.image = ImageResourceFactory.getImage(imageKey);
		this.flyingConfig = flyingConfig;
	}

	protected Flying() {
	}

	public BufferedImage getImage() {
		return (null == image) ? ImageResourceFactory.getImage(imageKey) : image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public FlyingConfig getConfig() {
		if (null == flyingConfig) {
			throw new AsoulException("当前飞行物未指定配置");
		}
		return flyingConfig;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
		this.image = ImageResourceFactory.getImage(imageKey);
	}

	public void setConfig(FlyingConfig flyingConfig) {
		this.flyingConfig = flyingConfig;
	}

	/**
	 * @Description 移动至指定左边
	 * @Author Enchantedyou
	 * @Date 2021/11/20-17:25
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y) {
		flyingConfig.setX(x);
		flyingConfig.setY(y);
	}

	/**
	 * @Description 按照配置中的速度进行移动
	 * @Author Enchantedyou
	 * @Date 2021/11/28-20:37
	 */
	public void move() {
		// 计算角度
		double angle = Math.toRadians(flyingConfig.getDegrees());
		int xOffset = (int) (Math.cos(angle) * flyingConfig.getSpeed());
		int yOffset = (int) -(Math.sin(angle) * flyingConfig.getSpeed());

		// 反转处理
		if (xReverse) {
			xOffset = -xOffset;
		}
		if (yReverse) {
			yOffset = -yOffset;
		}
		moveTo(flyingConfig.getX() + xOffset, flyingConfig.getY() + yOffset);
	}

	/**
	 * @Description x轴运动方向反转
	 * @Author Enchantedyou
	 * @Date 2021/12/5-22:00
	 */
	public void xReverse() {
		xReverse = !xReverse;
	}

	/**
	 * @Description y轴运动方向反转
	 * @Author Enchantedyou
	 * @Date 2021/12/5-22:01
	 */
	public void yReverse() {
		yReverse = !yReverse;
	}
}
