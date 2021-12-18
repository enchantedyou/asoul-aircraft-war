package best.asoul.aircraft.invoker;

import java.io.File;

/**
 * @Description 文件加载回调函数
 * @Author Enchantedyou
 * @Date 2021年11月20日-14:28
 */
public interface FileLoadInvoker {

	/**
	 * @Description 对读取到的文件做些什么
	 * @Author Enchantedyou
	 * @Date 2021/11/20-14:28
	 * @param file
	 */
	void invoke(File file);
}
