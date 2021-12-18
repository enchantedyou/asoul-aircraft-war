package best.asoul.aircraft.config;

/**
 * @Description 全局配置
 * @Author Enchantedyou
 * @Date 2021年11月20日-16:06
 */
public class GlobalConfig {

	private GlobalConfig() {
	}

	/** 屏幕宽度 **/
	public static final int SCREEN_WIDTH = 1920;
	/** 屏幕高度 **/
	public static final int SCREEN_HEIGHT = 1080;
	/** 核心线程数：逻辑线程数×4 **/
	public static final int CORE_THREAD_SIZE = Runtime.getRuntime().availableProcessors() << 2;
	/** 最大线程数：逻辑线程数×8 **/
	public static final int MAX_THREAD_SIZE = Runtime.getRuntime().availableProcessors() << 4;
	/** 空闲线程的存活时间 **/
	public static final long KEEP_ALIVE_SECONDS = 60L;
	/** 队列中的最大线程数：逻辑线程数×8 **/
	public static final int QUEUE_THREAD_SIZE = Runtime.getRuntime().availableProcessors() << 2;
}
