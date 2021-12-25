package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.bullet.*;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.BossType;
import best.asoul.aircraft.handler.bullet.ShotToPlayerDirectionHandler;
import best.asoul.aircraft.handler.bullet.StraightWithReflectHandler;
import best.asoul.aircraft.handler.bullet.WhirlpoolSpreadShotHandler;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.SoundUtil;

/**
 * @Description 小boss：普信羊驼
 * @Author Enchantedyou
 * @Date 2021/12/19-15:55
 */
public class BossToolsPeople extends EnemyAircraft {

	public BossToolsPeople(int maxHealthPoint) {
		super(ResourceConst.BOSS_TOOLS_PEOPLE, new LittleRectangleBullet(), maxHealthPoint);
		flyingConfig.setMaxEnemyY(350);
		flyingConfig.determineSize(300, 300);
		camp = AircraftCamp.BOSS;
		setBossType(BossType.HALFWAY);
	}

	public BossToolsPeople() {
		this(612000);
	}

	@Override
	public void afterDead() {
		AsoulThreadHelper.submitTask(() -> {
			super.afterDead();
			// 小boss后切换到常规bgm
			AsoulUtil.pause(GlobalConst.BOSS_DEAD_EXPLODE_INTERVAL * (GlobalConst.BOSS_DEAD_EXPLODE_COUNT + 2));
			SoundUtil.loopAdventureBgm();
		});
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		shotChain.append(new StraightWithReflectHandler(true, 15, 6000L, 50L, -30D, -60D, -90D,
				-120D, -150D).setBeforeNextHandleInvoker(() -> setBullet(new MidArcBullet())));
		shotChain.append(new ShotToPlayerDirectionHandler(25, 40, 30, 3, 1000L));
		shotChain.append(new WhirlpoolSpreadShotHandler(15, 3, 1200L, true, 10)
				.setBeforeNextHandleInvoker(() -> setBullet(new LittleBallBullet())));
		shotChain.append(new WhirlpoolSpreadShotHandler(10, 3, 1500L, false, 10)
				.setBeforeNextHandleInvoker(() -> setBullet(new LongOvalBullet())));
		shotChain.append(new ShotToPlayerDirectionHandler(50, 20, 25, 2, 1000L));
		shotChain.append(new WhirlpoolSpreadShotHandler(10, 5, 600L, false, 22));
		shotChain.append(new StraightWithReflectHandler(true, 35, 10000L, 30L, -15D, -45D,
				-90D, -135D, -165D).setBeforeNextHandleInvoker(() -> setBullet(new BossWaveBullet())));
		shotChain.append(new WhirlpoolSpreadShotHandler(30, 2, 1000L, true, 3)
				.setBeforeNextHandleInvoker(() -> setBullet(new MidArcBullet())));
		shotChain.append(new WhirlpoolSpreadShotHandler(12, 90, 300L, false, 8));
		shotChain.append(new ShotToPlayerDirectionHandler(999, 30, 20, 0, 10L));
	}
}
