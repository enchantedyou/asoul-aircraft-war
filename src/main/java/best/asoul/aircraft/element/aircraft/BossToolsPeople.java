package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.bullet.*;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.handler.bullet.ShotToPlayerDirectionHandler;
import best.asoul.aircraft.handler.bullet.StraightWithReflectHandler;
import best.asoul.aircraft.handler.bullet.WhirlpoolSpreadShotHandler;

/**
 * @Description 大boss：普信羊驼
 * @Author Enchantedyou
 * @Date 2021/12/19-15:55
 */
public class BossToolsPeople extends EnemyAircraft {

	public BossToolsPeople(int maxHealthPoint) {
		super(ResourceConst.BOSS_TOOLS_PEOPLE, new LittleRectangleBullet(), maxHealthPoint);
		flyingConfig.setMaxEnemyY(350);
		camp = AircraftCamp.BOSS;
	}

	public BossToolsPeople() {
		this(1888888);
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		final int turnCount = 30;
		for (int i = 0; i < turnCount; i++) {
			// 这里的射击处理器并没有什么实际含义，只是为了方便设置回调函数，所以命名简写
			final StraightWithReflectHandler s1 = new StraightWithReflectHandler(true, 15, 6000L, 50L, -30D, -60D, -90D,
					-120D, -150D);
			s1.setBeforeNextHandleInvoker(() -> setBullet(new MidArcBullet()));
			shotChain.append(s1);
			shotChain.append(new ShotToPlayerDirectionHandler(25, 40, 30, 3, 1000L));
			final WhirlpoolSpreadShotHandler s2 = new WhirlpoolSpreadShotHandler(15, 3, 1200L, true, 10);
			s2.setBeforeNextHandleInvoker(() -> setBullet(new LittleBallBullet()));
			shotChain.append(s2);
			shotChain.append(new WhirlpoolSpreadShotHandler(10, 3, 1500L, false, 10));
			final ShotToPlayerDirectionHandler s3 = new ShotToPlayerDirectionHandler(30, 30, 45, 2, 100L);
			s3.setBeforeNextHandleInvoker(() -> setBullet(new LongOvalBullet()));
			shotChain.append(s3);
			shotChain.append(new ShotToPlayerDirectionHandler(50, 20, 25, 2, 1000L));
			shotChain.append(new WhirlpoolSpreadShotHandler(10, 5, 600L, false, 22));
			final StraightWithReflectHandler s4 = new StraightWithReflectHandler(true, 35, 10000L, 30L, -15D, -45D,
					-90D, -135D, -165D);
			s4.setBeforeNextHandleInvoker(() -> setBullet(new BossWaveBullet()));
			shotChain.append(s4);
			final WhirlpoolSpreadShotHandler s5 = new WhirlpoolSpreadShotHandler(30, 2, 1000L, true, 3);
			s5.setBeforeNextHandleInvoker(() -> setBullet(new MidArcBullet()));
			shotChain.append(s5);
			shotChain.append(new WhirlpoolSpreadShotHandler(12, 90, 300L, false, 8));
		}
		shotChain.append(new ShotToPlayerDirectionHandler(999, 30, 20, 0, 10L));
	}
}
