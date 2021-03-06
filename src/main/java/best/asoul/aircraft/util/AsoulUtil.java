package best.asoul.aircraft.util;

import java.io.*;
import java.util.concurrent.locks.LockSupport;

import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.boost.*;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.Direction;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 全局工具类
 * @Author Enchantedyou
 * @Date 2021年11月20日-23:06
 */
@Slf4j
public class AsoulUtil {

	private AsoulUtil() {
	}

	/**
	 * @param obj
	 * @param clazz
	 * @return T
	 * @Description 对象克隆
	 * @Author Enchantedyou
	 * @Date 2021/11/20-23:10
	 */
	public static <T> T clone(T obj, Class<T> clazz) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(obj);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			try (ObjectInputStream ois = new ObjectInputStream(bis)) {
				return clazz.cast(ois.readObject());
			}
		} catch (Exception e) {
			throw new AsoulException("对象深拷贝失败", e);
		}
	}

	/**
	 * @Description 获取流的字节数组
	 * @Author Enchantedyou
	 * @Date 2021/12/23-23:32
	 * @param inputStream
	 * @return java.io.InputStream
	 */
	public static byte[] getStreamByteArray(InputStream inputStream) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > -1) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			throw new AsoulException("获取流的字节数组失败", e);
		}
	}

	/**
	 * @param millis
	 * @Description 暂停指定时长（毫秒）
	 * @Author Enchantedyou
	 * @Date 2021/11/22-22:39
	 */
	public static void pause(long millis) {
		if (millis <= 0L) {
			return;
		}

		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * @return boolean
	 * @Description 概率触发
	 * @Author Enchantedyou
	 * @Date 2021/11/28-17:30
	 */
	public static boolean rand(int rate) {
		return GlobalConst.RANDOM.nextInt(100) < rate;
	}

	/**
	 * @param enemy
	 * @Description 随机产生游离态增益效果
	 * @Author Enchantedyou
	 * @Date 2021/12/4-13:12
	 */
	public static void randCreateDriftBoost(Aircraft enemy) {
		if (enemy.getCamp() == AircraftCamp.ASOUL || !rand(GlobalConst.BOOST_CREATE_PROBABILITY)) {
			return;
		}

		DriftBoot driftBoot;
		if (rand(40)) {
			driftBoot = new BulletLevelUpDriftBoost(enemy);
		} else if (rand(15)) {
			driftBoot = new AircraftTreatmentDriftBoost(enemy);
		} else if (rand(20)) {
			driftBoot = new AircraftShieldDriftBoost(enemy);
		} else {
			driftBoot = new AvaDriftBoost(enemy);
		}
		GameContext.getStageDefine().getDriftBootList().add(driftBoot);
	}

	/**
	 * @Description 计算两点之间的距离
	 * @Author Enchantedyou
	 * @Date 2021/12/5-16:18
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return double
	 */
	public static double calcDistanceBetweenPoints(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
	}

	/**
	 * @Description 计算敌机射击角度
	 * @Author Enchantedyou
	 * @Date 2021/12/19-0:19
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return double
	 */
	public static double calcEnemyShotDegrees(int x1, int y1, int x2, int y2) {
		double hypotenuseDistance = calcDistanceBetweenPoints(x1, y1, x2, y2);
		double horizontalDistance = calcDistanceBetweenPoints(x1, 0, x2, 0);

		final double cosDegrees = Math.toDegrees(Math.acos(horizontalDistance / hypotenuseDistance));
		final double sinDegrees = Math.toDegrees(Math.asin(horizontalDistance / hypotenuseDistance));

		if (y2 > y1) {
			if (x2 > x1) {
				// 第二象限
				return GlobalConst.MAX_QUADRANT_DEGREES * 2 - cosDegrees;
			} else if (x2 < x1) {
				// 第一象限
				return cosDegrees;
			} else {
				return Direction.UP.degrees();
			}
		} else if (y2 < y1) {
			if (x2 > x1) {
				// 第三象限
				return GlobalConst.MAX_QUADRANT_DEGREES * 3 - sinDegrees;
			} else if (x2 < x1) {
				// 第四象限
				return GlobalConst.MAX_QUADRANT_DEGREES * 3 + sinDegrees;
			} else {
				return Direction.DOWN.degrees();
			}
		} else {
			return x1 > x2 ? Direction.RIGHT.degrees() : Direction.LEFT.degrees();
		}
	}

	/**
	 * @Description 允许当前线程受暂停控制
	 * @Author Enchantedyou
	 * @Date 2021/12/22-22:22
	 */
	public static void enablePause() {
		if (AsoulThreadHelper.isGamePause()) {
			LockSupport.park();
		}
	}
}