package best.asoul.aircraft.entity;

/**
 * @Description 三角函数中的象限
 * @Author Enchantedyou
 * @Date 2021年12月05日-22:12
 */
public enum Quadrant {

	/** 第一象限 **/
	ONE(0D),
	/** 第二象限 **/
	TWO(90D),
	/** 第三象限 **/
	THREE(180D),
	/** 第四象限 **/
	FOUR(270D),
	/** 不属于任意一个象限 **/
	NONE(0D);

	/** 角度偏移量 **/
	private double degreesOffset;

	private Quadrant(double degreesOffset) {
		this.degreesOffset = degreesOffset;
	}

	public double degreesOffset() {
		return this.degreesOffset;
	}
}
