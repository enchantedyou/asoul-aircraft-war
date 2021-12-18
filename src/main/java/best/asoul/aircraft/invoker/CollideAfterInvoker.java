package best.asoul.aircraft.invoker;

/**
 * @Description 碰撞后回调，包括子弹和飞机碰撞，飞机和飞机碰撞
 * @Author Enchantedyou
 * @Date 2021/11/26-18:04
 */
public interface CollideAfterInvoker {

	/**
	 * @Description 碰撞后做些什么呢
	 * @Author Enchantedyou
	 * @Date 2021/11/26-18:05
	 */
	void invoke();
}
