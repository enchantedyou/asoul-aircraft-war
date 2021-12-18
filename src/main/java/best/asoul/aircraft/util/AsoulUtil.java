package best.asoul.aircraft.util;

import java.io.*;

import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.boost.*;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.Quadrant;
import best.asoul.aircraft.exception.AsoulException;
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
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);

			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bis);
			return clazz.cast(ois.readObject());
		} catch (Exception e) {
			log.error("对象深拷贝失败", e);
			throw new AsoulException(e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// 忽略
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// 忽略
				}
			}
		}
	}

	/**
	 * @param millis
	 * @Description 暂停指定时长（毫秒）
	 * @Author Enchantedyou
	 * @Date 2021/11/22-22:39
	 */
	public static void pause(long millis) {
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
		if (enemy.getCamp() != AircraftCamp.ENEMY || !rand(GlobalConst.BOOST_CREATE_PROBABILITY)) {
			return;
		}

		DriftBoot driftBoot;
		if (rand(35)) {
			driftBoot = new BulletLevelUpDriftBoost(enemy);
		} else if (rand(15)) {
			driftBoot = new AircraftTreatmentDriftBoost(enemy);
		} else if (rand(10)) {
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
	 * @Description 根据象限计算实际角度
	 * @Author Enchantedyou
	 * @Date 2021/12/5-22:15
	 * @param degrees
	 * @param quadrant
	 * @return double
	 */
	public static double getActualDegrees(double degrees, Quadrant quadrant) {
		return degrees + quadrant.degreesOffset();
	}
}