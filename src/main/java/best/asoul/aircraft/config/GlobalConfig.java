package best.asoul.aircraft.config;

import java.util.regex.Pattern;

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
	public static final int MAX_THREAD_SIZE = Runtime.getRuntime().availableProcessors() << 3;
	/** 空闲线程的存活时间 **/
	public static final long KEEP_ALIVE_SECONDS = 60L;
	/** 队列中的最大线程数：逻辑线程数×8 **/
	public static final int QUEUE_THREAD_SIZE = Runtime.getRuntime().availableProcessors() << 3;

	/** 加密方式 **/
	public static final String SECRET_METHOD = "AES";
	/** 偏移量 **/
	public static final String SECRET_IV = "0307612717808112";
	/** 加密实例 **/
	public static final String CIPHER_INSTANCE = "AES/CBC/PKCS5Padding";
	/** 加密文件分割开始标志 **/
	public static final String SECRET_START_TOKEN = "AS_START";
	/** 加密文件分割结束标志 **/
	public static final String SECRET_END_TOKEN = "AS_END";
	/** 加密资源拆分正则表达式 **/
	public static final Pattern SECRET_RESOURCE_SPLIT_PATTERN =
			Pattern.compile(SECRET_START_TOKEN + "(.*?)" + SECRET_END_TOKEN);
	/** 加密文件后缀 **/
	public static final String SECRET_FILE_SUFFIX = "as";
	/** 加密秘钥 **/
	public static final String SECRET_KEY = "";

	/** 用户配置文件名称 **/
	public static final String USER_CONFIG_FILE_NAME = "asoul.properties";
	/** 用户配置属性前缀 **/
	public static final String USER_CONFIG_PROP_PREFIX = "asoul.user.config";
}
