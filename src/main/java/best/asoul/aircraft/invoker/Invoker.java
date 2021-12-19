package best.asoul.aircraft.invoker;

import java.io.Serializable;

/**
 * @Description 默认回调
 * @Author Enchantedyou
 * @Date 2021/11/22-22:24
 */
public interface Invoker extends Serializable {

	/**
	 * @Description 回调处理
	 * @Author Enchantedyou
	 * @Date 2021/11/22-22:25
	 */
	void invoke();
}
