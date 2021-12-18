package best.asoul.aircraft.entity;

import java.io.Serializable;

/**
 * @Description 持有态增益效果状态
 * @Author Enchantedyou
 * @Date 2021年12月03日-23:38
 */
public class HoldBoostState implements Serializable {

	/** 特效开始时间 **/
	private long startTime;
	/** 特效持续时间 **/
	private long remainDuration;
	/** 特效过期时间 **/
	private long expireTime;

	public HoldBoostState(long startTime, long duration) {
		this.startTime = startTime;
		this.remainDuration = duration;
		this.expireTime = startTime + duration;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getRemainDuration() {
		return remainDuration;
	}

	public void setRemainDuration(long remainDuration) {
		this.remainDuration = remainDuration;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return "HoldBoostState{" +
				"startTime=" + startTime +
				", remainDuration=" + remainDuration +
				", expireTime=" + expireTime +
				'}';
	}
}
