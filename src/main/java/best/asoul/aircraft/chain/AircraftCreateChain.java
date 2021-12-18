package best.asoul.aircraft.chain;

import java.util.LinkedList;
import java.util.List;

import best.asoul.aircraft.handler.aircraft.AircraftCreateHandler;

/**
 * @Description 战机生成的链，控制关卡如何生成战机
 * @Author Enchantedyou
 * @Date 2021年11月22日-21:48
 */
public class AircraftCreateChain {

	/** 战机生成处理器链表 **/
	private List<AircraftCreateHandler> aircraftCreateHandlerList = new LinkedList<>();

	public AircraftCreateChain(AircraftCreateHandler firstHandler) {
		aircraftCreateHandlerList.add(firstHandler);
	}

	public AircraftCreateChain() {
	}

	/**
	 * @Description 执行该责任链生成战机
	 * @Author Enchantedyou
	 * @Date 2021/11/22-22:07
	 */
	public void doChain() {
		for (AircraftCreateHandler currentHandler : aircraftCreateHandlerList) {
			// 执行创建任务
			currentHandler.create();
			// 下一个任务执行前的处理
			currentHandler.beforeNextHandle();
		}
	}

	/**
	 * @Description 追加战机生成处理器
	 * @Author Enchantedyou
	 * @Date 2021/12/5-17:25
	 * @param handler
	 */
	public AircraftCreateChain append(AircraftCreateHandler handler) {
		if (null != handler) {
			this.aircraftCreateHandlerList.add(handler);
		}
		return this;
	}
}
