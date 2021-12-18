package best.asoul.aircraft.handler.bullet;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.invoker.Invoker;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 子弹射击处理器
 * @Author Enchantedyou
 * @Date 2021年12月06日-20:35
 */
public abstract class ShotHandler implements Serializable {

	/** 下一个处理器调用前的处理 **/
	protected transient Invoker beforeNextHandleInvoker;
	/** 战机阵营限制 **/
	protected AircraftCamp campLimit;
	/** 总共发射多少轮 **/
	protected int turnCount;
	/** 每一轮间隔多久 **/
	protected long turnInterval;

	protected ShotHandler(AircraftCamp campLimit, int turnCount, long turnInterval) {
		this.campLimit = campLimit;
		this.turnCount = turnCount;
		this.turnInterval = turnInterval;
	}

	/**
	 * @Description 射击
	 * @Author Enchantedyou
	 * @Date 2021/12/6-20:52
	 */
	public abstract void shot(Aircraft aircraft);

	/**
	 * @Description 创建一颗子弹，位置处于发射起始点
	 * @Author Enchantedyou
	 * @Date 2021/12/6-21:00
	 * @return best.asoul.aircraft.element.base.Bullet
	 */
	protected Bullet createBullet(Aircraft aircraft) {
		if (!aircraft.getCamp().equals(campLimit)) {
			throw new AsoulException("战机所属阵营无效");
		}

		final FlyingConfig aircraftConfig = aircraft.getConfig();
		// 复制当前战机的子弹属性
		final Bullet bullet = AsoulUtil.clone(aircraft.getBullet(), Bullet.class);
		final BufferedImage bulletImage = bullet.getImage();

		int x = aircraftConfig.getX() + (aircraft.getImage().getWidth() - bulletImage.getWidth()) / 2;
		int y = 0;
		// 指定当前子弹的位置
		if (aircraft.getCamp() == AircraftCamp.ASOUL) {
			y = aircraftConfig.getY() - bulletImage.getHeight() + 120;
		} else {
			y = aircraftConfig.getY() + bulletImage.getHeight() + 20;
		}
		bullet.moveTo(x, y);

		// 添加进列表前的处理
		beforeAppendBullet(bullet);
		// 添加进子弹列表
		aircraft.getShotList().add(bullet);
		return bullet;
	}

	/**
	 * @Description 子弹添加进列表前的处理
	 * @Author Enchantedyou
	 * @Date 2021/12/7-22:28
	 * @param bullet
	 */
	protected void beforeAppendBullet(Bullet bullet) {
		// 默认空处理
	}

	/**
	 * @Description 下一轮射击前做些什么
	 * @Author Enchantedyou
	 * @Date 2021/12/6-21:12
	 */
	public void beforeNextHandle() {
		if (beforeNextHandleInvoker != null) {
			beforeNextHandleInvoker.invoke();
		} else {
			AsoulUtil.pause(1200L);
		}
	}

	public Invoker getBeforeNextHandleInvoker() {
		return beforeNextHandleInvoker;
	}

	public void setBeforeNextHandleInvoker(Invoker beforeNextHandleInvoker) {
		this.beforeNextHandleInvoker = beforeNextHandleInvoker;
	}
}
