package best.asoul.aircraft.invoker;

import java.io.ByteArrayInputStream;

/**
 * @Description 资源解密后处理
 * @Author Enchantedyou
 * @Date 2021/12/23-13:41
 */
public interface ResourceDecodeInvoker {

	/**
	 * @Description 资源解密后处理
	 * @Author Enchantedyou
	 * @Date 2021/12/23-13:42
	 * @param inputStream
	 * @param fileName
	 */
	void invoke(String fileName, ByteArrayInputStream inputStream);
}
