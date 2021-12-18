package best.asoul.aircraft.exception;

/**
 * @Description 全局运行时异常
 * @Author Enchantedyou
 * @Date 2021年11月20日-13:15
 */
public class AsoulException extends RuntimeException {

	public AsoulException(Throwable e) {
		super(e);
	}

	public AsoulException(String message, Throwable e) {
		super(message, e);
	}

	public AsoulException(String message) {
		super(message);
	}
}
