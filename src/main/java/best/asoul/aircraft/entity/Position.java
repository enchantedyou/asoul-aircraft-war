package best.asoul.aircraft.entity;

/**
 * @Description 位置坐标
 * @Author Enchantedyou
 * @Date 2021年11月20日-22:30
 */
public class Position {

	/** x轴坐标 **/
	private int x;
	/** y轴坐标 **/
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
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

	@Override
	public String toString() {
		return "Position{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
