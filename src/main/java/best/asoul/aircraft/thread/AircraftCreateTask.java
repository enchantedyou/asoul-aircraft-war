package best.asoul.aircraft.thread;

import best.asoul.aircraft.chain.AircraftCreateChain;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 执行生成战机的责任链
 * @Author Enchantedyou
 * @Date 2021/11/21-17:38
 */
@Slf4j
public class AircraftCreateTask implements Runnable {

	private AircraftCreateChain chain;

	public AircraftCreateTask(AircraftCreateChain chain) {
		this.chain = chain;
	}

	@Override
	public void run() {
		try {
			chain.doChain();
		} catch (Exception e) {
			log.error("战机生成失败", e);
		}
	}
}
