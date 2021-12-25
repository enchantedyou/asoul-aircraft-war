package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.bullet.BossWaveBullet;
import best.asoul.aircraft.element.bullet.LongOvalBullet;
import best.asoul.aircraft.element.bullet.MidArcBullet;
import best.asoul.aircraft.element.bullet.SkeletonBullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.BossType;
import best.asoul.aircraft.handler.bullet.ShotToPlayerDirectionHandler;
import best.asoul.aircraft.handler.bullet.StraightWithReflectHandler;
import best.asoul.aircraft.handler.bullet.WhirlpoolSpreadShotHandler;

/**
 * @Description 骷髅boss
 * @Author Enchantedyou
 * @Date 2021/12/24-22:28
 */
public class BossSkeleton extends EnemyAircraft {

	public BossSkeleton(int maxHealthPoint) {
		super(ResourceConst.BOSS_SKELETON, new MidArcBullet(), maxHealthPoint);
		camp = AircraftCamp.BOSS;
		setBossType(BossType.FINAL);
		flyingConfig.setMaxEnemyY(350);
		flyingConfig.determineSize(370, 300);
	}

	public BossSkeleton() {
		this((int) (1211000 * 1.5));
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		final int turnCount = 30;
		for (int i = 0; i < turnCount; i++) {
			shotChain.append(new WhirlpoolSpreadShotHandler(15, 3, 1200L, true, 8));
			shotChain.append(new StraightWithReflectHandler(true, 12, 6000L, 50L, -30D, -60D, -90D, -120D, -150D));
			shotChain.append(new WhirlpoolSpreadShotHandler(6, 8, 0L, false, 12, 10L)
					.setBeforeNextHandleInvoker(() -> setBullet(new SkeletonBullet())));
			shotChain.append(new ShotToPlayerDirectionHandler(30, 60, 16, 2, 200L));
			shotChain.append(new WhirlpoolSpreadShotHandler(10, 5, 600L, false, 21)
					.setBeforeNextHandleInvoker(() -> setBullet(new LongOvalBullet())));
			shotChain.append(new StraightWithReflectHandler(true, 35, 10000L, 30L, -15D, -45D,
					-90D, -135D, -165D).setBeforeNextHandleInvoker(() -> setBullet(new BossWaveBullet())));
			shotChain.append(new WhirlpoolSpreadShotHandler(30, 2, 1000L, true, 3)
					.setBeforeNextHandleInvoker(() -> setBullet(new MidArcBullet())));
			shotChain.append(new WhirlpoolSpreadShotHandler(20, 90, 400L, false, 9));
		}
		shotChain.append(new ShotToPlayerDirectionHandler(999, 80, 20, 0, 10L));
	}
}
