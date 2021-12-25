package best.asoul.aircraft.thread.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description asoul自定义线程工厂
 * @Author Enchantedyou
 * @Date 2021年11月24日-21:43
 */
@Slf4j
public class AsoulThreadFactory implements ThreadFactory {

	private AtomicInteger poolNo = new AtomicInteger(1);
	private AtomicInteger threadNo = new AtomicInteger(1);
	private String prefix;
	/** 是否追加到线程任务列表，列表中的线程会随着游戏的暂停而暂停 **/
	private boolean appendTaskList;

	public AsoulThreadFactory(boolean appendTaskList) {
		prefix = String.format("asoul-%s-thread-", poolNo.getAndIncrement());
		this.appendTaskList = appendTaskList;
	}

	@Override
	public Thread newThread(Runnable r) {
		final Thread thread = new Thread(r);
		thread.setName(prefix + threadNo.getAndIncrement());
		// 加入到线程列表，用于暂停和恢复线程池中的线程。入侵并操作线程池中的线程是下下策，但是在该场景中暂未找到更合适的暂停实现方式
		if (appendTaskList) {
			AsoulThreadHelper.TASK_LIST.add(thread);
		}
		log.debug("线程池创建线程：{}", thread.getName());
		return thread;
	}
}
