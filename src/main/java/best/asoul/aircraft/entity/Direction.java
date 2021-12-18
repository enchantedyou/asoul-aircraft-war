package best.asoul.aircraft.entity;

/**
 * @Description 方向
 * @Author Enchantedyou
 * @Date 2021/12/5-21:46
 * @return
 */
public enum Direction {

	/** 上 **/
	UP(90D),
	/** 下 **/
	DOWN(270D),
	/** 左 **/
	LEFT(180D),
	/** 右 **/
	RIGHT(0D);

	private double degrees;

	private Direction(double degrees) {
		this.degrees = degrees;
	}

	public double degrees() {
		return degrees;
	}
}
