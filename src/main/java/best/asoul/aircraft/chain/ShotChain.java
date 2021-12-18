package best.asoul.aircraft.chain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.handler.bullet.ShotHandler;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 飞机射击的链，控制射击规则
 * @Author Enchantedyou
 * @Date 2021年12月06日-21:05
 */
public class ShotChain implements Serializable {

	/** 射击处理器链表 **/
	private List<ShotHandler> shotHandlerList = new LinkedList<>();
	/** 要发射子弹的战机 **/
	private Aircraft aircraft;

	public ShotChain(Aircraft aircraft, ShotHandler firstHandler) {
		this.aircraft = aircraft;
		append(firstHandler);
	}

	public ShotChain(Aircraft aircraft) {
		this(aircraft, null);
	}

	/**
	 * @Description 执行责任链进行射击
	 * @Author Enchantedyou
	 * @Date 2021/12/6-21:13
	 */
	public void doChain() {
		// 未达到指定位置前不射击
		while ((aircraft.getImage().getHeight() + aircraft.getConfig().getY()) < aircraft.getConfig()
				.getMaxEnemyY() && !Thread.currentThread().isInterrupted()) {
			if (aircraft.isDead()) {
				return;
			}
			AsoulUtil.pause(50L);
		}
		for (ShotHandler shotHandler : shotHandlerList) {
			// 开始射击
			shotHandler.shot(aircraft);
			// 下一轮射击前的处理
			shotHandler.beforeNextHandle();
		}
	}

	/**
	 * @Description 追加射击处理器
	 * @Author Enchantedyou
	 * @Date 2021/12/6-21:09
	 * @param shotHandler
	 * @return best.asoul.aircraft.chain.ShotChain
	 */
	public ShotChain append(ShotHandler shotHandler) {
		if (shotHandler != null) {
			shotHandlerList.add(shotHandler);
		}
		return this;
	}

	public List<ShotHandler> getShotHandlerList() {
		return shotHandlerList;
	}
}
