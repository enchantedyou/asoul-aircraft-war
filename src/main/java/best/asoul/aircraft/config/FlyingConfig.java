package best.asoul.aircraft.config;

import java.io.Serializable;

/**
 * @Description 飞行物默认属性
 * @Author Enchantedyou
 * @Date 2021年11月20日-16:09
 */
public abstract class FlyingConfig implements Serializable {

	/** 宽 **/
	protected int width;
	/** 高 **/
	protected int height;
	/** x坐标 **/
	protected int x;
	/** y坐标 **/
	protected int y;

	/** 移动间隔 **/
	protected int moveInterval;
	/** 子弹或战机生成间隔 **/
	protected int createInterval;
	/** 移动速度 **/
	protected double speed;
	/** 移动角度 **/
	protected double degrees;
	/** 敌机可飞行的最大Y轴坐标（maxEnemyY=当前Y轴坐标+贴图的高） **/
	protected int maxEnemyY;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getMoveInterval() {
		return moveInterval;
	}

	public void setMoveInterval(int moveInterval) {
		this.moveInterval = moveInterval;
	}

	public int getCreateInterval() {
		return createInterval;
	}

	public void setCreateInterval(int createInterval) {
		this.createInterval = createInterval;
	}

	public void determineSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void determinePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getDegrees() {
		return degrees;
	}

	public void setDegrees(double degrees) {
		this.degrees = degrees;
	}

	public int getMaxEnemyY() {
		return maxEnemyY;
	}

	public void setMaxEnemyY(int maxEnemyY) {
		this.maxEnemyY = maxEnemyY;
	}

	/**
	 * @Description 速度增加
	 * @Author Enchantedyou
	 * @Date 2021/12/5-21:52
	 * @param v
	 */
	public void increaseSpeed(double v) {
		this.speed += v;
	}
}
