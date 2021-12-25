package best.asoul.aircraft.handler.aircraft;

import java.util.List;

import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.invoker.Invoker;
import best.asoul.aircraft.thread.BulletCreateTask;
import best.asoul.aircraft.thread.BulletMoveTask;
import best.asoul.aircraft.thread.EnemyAircraftMoveTask;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;

/**
 * @Description 战机生成处理器
 * @Author Enchantedyou
 * @Date 2021年11月22日-21:22
 */
public abstract class AircraftCreateHandler {

	/** 创建数量 **/
	protected int count;
	/** 要创建的战机实例 **/
	protected Aircraft aircraft;
	/** 已存在的战机列表 **/
	protected List<Aircraft> aircraftList;
	/** 下一个处理器调用前的处理 **/
	protected Invoker beforeNextHandleInvoker;

	/**
	 * @Description 下一个处理器处理前做些什么
	 * @Author Enchantedyou
	 * @Date 2021/11/22-22:27
	 */
	public void beforeNextHandle() {
		if (beforeNextHandleInvoker != null) {
			beforeNextHandleInvoker.invoke();
		}
	}

	/**
	 * @Description 创建战机
	 * @Author Enchantedyou
	 * @Date 2021/11/22-21:43
	 */
	public Aircraft create() {
		initAircraft(aircraft);
		return aircraft;
	}

	protected AircraftCreateHandler(int count, Aircraft aircraft, List<Aircraft> aircraftList) {
		this.count = count;
		this.aircraft = aircraft;
		this.aircraftList = aircraftList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Aircraft getAircraft() {
		return aircraft;
	}

	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	public List<Aircraft> getAircraftList() {
		return aircraftList;
	}

	public void setAircraftList(List<Aircraft> aircraftList) {
		this.aircraftList = aircraftList;
	}

	public void setBeforeNextHandleInvoker(Invoker beforeNextHandleInvoker) {
		this.beforeNextHandleInvoker = beforeNextHandleInvoker;
	}

	/**
	 * @Description 追加战机至列表，附带创建子弹生成线程
	 * @Author Enchantedyou
	 * @Date 2021/11/23-22:35
	 * @param aircraft
	 * @return best.asoul.aircraft.element.base.Aircraft
	 */
	protected final void initAircraft(Aircraft aircraft) {
		if (aircraft.getCamp() != AircraftCamp.ASOUL) {
			AsoulThreadHelper.readyAwait();
		}
		// 添加至战机列表
		aircraftList.add(aircraft);
		// 创建子弹的生成线程
		AsoulThreadHelper.submitTask(new BulletCreateTask(aircraft));
		// 创建战机移动的线程
		AsoulThreadHelper.submitTask(new EnemyAircraftMoveTask(aircraft));
		// 创建子弹移动的线程
		AsoulThreadHelper.submitTask(new BulletMoveTask(aircraft));
	}
}
