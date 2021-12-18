package best.asoul.aircraft.handler.aircraft;

import java.util.List;

import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.base.Aircraft;

/**
 * @Description 弧线穿越的阵型创建战机（从左从左至右，从右往左的摆烂了懒得写了）
 * @Author Enchantedyou
 * @Date 2021年12月08日-22:19
 */
public class ArcCrossingCreateHandler extends AircraftCreateHandler {

	/** 切入角度 **/
	private double inDegrees;
	/** 切出角度 **/
	private double outDegrees;

	protected ArcCrossingCreateHandler(int count, Aircraft aircraft, List<Aircraft> aircraftList,
			double inDegrees, double outDegrees) {
		super(count, aircraft, aircraftList);
		this.inDegrees = inDegrees;
		this.outDegrees = outDegrees;

		if (inDegrees % GlobalConst.MAX_QUADRANT_DEGREES == 0D || outDegrees % GlobalConst.MAX_QUADRANT_DEGREES == 0D) {
			throw new IllegalArgumentException("角度不合法");
		}
	}

	@Override
	public void create() {
		// 开摆！
	}
}
