package best.asoul.aircraft.handler.aircraft;

import java.util.List;

import best.asoul.aircraft.element.base.Aircraft;

/**
 * @Description 默认战机生成处理器
 * @Author Enchantedyou
 * @Date 2021/11/23-22:47
 */
public class DefaultAircraftCreateHandler extends AircraftCreateHandler {

	public DefaultAircraftCreateHandler(Aircraft aircraft, List<Aircraft> aircraftList) {
		super(1, aircraft, aircraftList);
	}

}
