package best.asoul.aircraft.handler.aircraft;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.aircraft.EnemyAircraft;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.BossType;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.SoundUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 随机生成战机
 * @Author Enchantedyou
 * @Date 2021/11/22-21:28
 */
@Slf4j
public class EnemyAircraftCreateHandler extends AircraftCreateHandler {

	/** 要创建的敌机 **/
	private EnemyAircraft enemyAircraft;
	/** 中间的飞机 **/
	private EnemyAircraft midAircraft;
	private static Map<Integer, Integer> midCountMap = new ConcurrentHashMap<>();
	/** 插入中间不同飞机最小支持的count **/
	private static final int MIN_SUPPORT_MID_COUNT = 3;
	/** 插入中间不同飞机最大支持的count **/
	private static final int MAX_SUPPORT_MID_COUNT = 9;

	public EnemyAircraftCreateHandler(int count, EnemyAircraft aircraft, EnemyAircraft midAircraft,
			List<Aircraft> aircraftList) {
		super(count, aircraft, aircraftList);
		enemyAircraft = aircraft;
		this.midAircraft = midAircraft;
		initMidCountMap();
	}

	public EnemyAircraftCreateHandler(int count, EnemyAircraft aircraft, List<Aircraft> aircraftList) {
		this(count, aircraft, null, aircraftList);
	}

	@Override
	public Aircraft create() {
		// boss出现前播放告警音效并卖几秒钟关子
		if (enemyAircraft.getCamp() == AircraftCamp.BOSS) {
			AsoulUtil.pause(1500L);
			SoundUtil.stopBgm();
			SoundUtil.playBossWarning();
			AsoulUtil.pause(2500L);

			if (enemyAircraft.getBossType() == BossType.HALFWAY) {
				SoundUtil.loopHalfwayBossBgm();
			} else if (enemyAircraft.getBossType() == BossType.FINAL) {
				SoundUtil.loopFinalBossBgm();
			} else {
				throw new AsoulException("未指定boss类型");
			}
		}

		final FlyingConfig aircraftConfig = aircraft.getConfig();
		if (count < 0) {
			log.warn("战机创建数量不合法：{}", count);
		} else if (count == 0) {
			while (!Thread.currentThread().isInterrupted()) {
				AsoulUtil.enablePause();
				doCreateAndMove(aircraft, aircraftConfig, true);
			}
		} else {
			final int everyAircraftWidth = GlobalConfig.SCREEN_WIDTH / (count);
			int offset = (everyAircraftWidth - aircraft.getImage().getWidth()) / 2;
			// 计算中间飞机的开始结束下标
			int midAircraftStartIndex = -1;
			final Integer midCount = midCountMap.get(count);
			if (midAircraft != null && count >= MIN_SUPPORT_MID_COUNT && count <= MAX_SUPPORT_MID_COUNT) {
				midAircraftStartIndex = (count - midCount) / 2;
			}
			// 以修正后的位置创建
			createWithFixedPosition(aircraftConfig, everyAircraftWidth, offset, midAircraftStartIndex, midCount);
		}
		return enemyAircraft;
	}

	private void createWithFixedPosition(FlyingConfig aircraftConfig, int everyAircraftWidth, int offset,
			int midAircraftStartIndex, Integer midCount) {
		for (int i = 0; i < count; i++) {
			AsoulUtil.enablePause();
			final Aircraft c;
			if (midAircraftStartIndex > -1 && i >= midAircraftStartIndex && i < midAircraftStartIndex + midCount) {
				c = doCreateAndMove(midAircraft, aircraftConfig, (i != count - 1));
			} else {
				c = doCreateAndMove(aircraft, aircraftConfig, (i != count - 1));
			}
			c.getConfig().setX(i * everyAircraftWidth + offset);
		}
	}

	private Aircraft doCreateAndMove(Aircraft sourceAircraft, FlyingConfig aircraftConfig, boolean pause) {
		final Aircraft clone = AsoulUtil.clone(sourceAircraft, Aircraft.class);
		determineAircraftPosition(clone);
		initAircraft(clone);
		if (pause) {
			AsoulUtil.pause(aircraftConfig.getCreateInterval());
		}
		return clone;
	}

	private void determineAircraftPosition(Aircraft aircraft) {
		// 计算敌机随机生成的位置
		final int width = aircraft.getImage().getWidth();
		final int height = aircraft.getImage().getHeight();
		int x = GlobalConst.RANDOM.nextInt(GlobalConfig.SCREEN_WIDTH);
		final int maxX = GlobalConfig.SCREEN_WIDTH - width;
		if (x < width) {
			x = width;
		} else if (x > maxX) {
			x = maxX;
		}
		// 移动至指定位置
		aircraft.moveTo(x, -height);
	}

	private void initMidCountMap() {
		midCountMap.put(MIN_SUPPORT_MID_COUNT, 1);
		midCountMap.put(4, 2);
		midCountMap.put(5, 1);
		midCountMap.put(6, 2);
		midCountMap.put(7, 3);
		midCountMap.put(8, 2);
		midCountMap.put(MAX_SUPPORT_MID_COUNT, 3);
	}
}
