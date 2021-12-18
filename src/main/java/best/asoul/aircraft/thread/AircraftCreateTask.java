package best.asoul.aircraft.thread;

import best.asoul.aircraft.chain.AircraftCreateChain;

/**
 * @Description 执行生成战机的责任链
 * @Author Enchantedyou
 * @Date 2021/11/21-17:38
 */
public class AircraftCreateTask implements Runnable {

	private AircraftCreateChain chain;

	public AircraftCreateTask(AircraftCreateChain chain) {
		this.chain = chain;
	}

	@Override
	public void run() {
		chain.doChain();
	}
}
