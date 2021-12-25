package best.asoul.aircraft.thread;

import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 子弹创建任务
 * @Author Enchantedyou
 * @Date 2021/11/21-15:49
 */
@Slf4j
public class BulletCreateTask implements Runnable {

	/** 子弹所属战机 **/
	private Aircraft aircraft;

	public BulletCreateTask(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public void run() {
		try {
			AsoulThreadHelper.readyAwait();
			aircraft.getShotChain().doChain();
		} catch (Exception e) {
			log.error("子弹创建失败", e);
		}
	}
}
