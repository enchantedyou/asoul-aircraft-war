package best.asoul.aircraft.element.base;

import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;

/**
 * @Description 子弹
 * @Author Enchantedyou
 * @Date 2021年11月20日-00:15
 */
public abstract class Bullet extends Flying implements Serializable {

	/** 攻击力 **/
	protected int attack;
	/** 碰到左右两边的墙是否反射 **/
	protected boolean leftRightReflect;

	protected Bullet(String imageKey, FlyingConfig bulletConfig) {
		super(imageKey, bulletConfig);
	}

	/**
	 * @Description 切换子弹等级
	 * @Author Enchantedyou
	 * @Date 2021/12/21-21:56
	 * @param bulletLevel
	 * @param awakeLevel
	 */
	public abstract void switchLevel(int bulletLevel, int awakeLevel);

	public synchronized void setAttack(int attack) {
		this.attack = attack;
	}

	/**
	 * @Description 攻击力提升
	 * @Author Enchantedyou
	 * @Date 2021/12/18-16:41
	 * @param attack
	 */
	public synchronized void increaseAttack(int attack) {
		this.attack += attack;
	}

	public synchronized int getAttack() {
		return attack;
	}

	public boolean isLeftRightReflect() {
		return leftRightReflect;
	}

	public void setLeftRightReflect(boolean leftRightReflect) {
		this.leftRightReflect = leftRightReflect;
	}
}
