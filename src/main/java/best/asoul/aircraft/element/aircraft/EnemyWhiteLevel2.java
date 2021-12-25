package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.bullet.LongOvalBullet;
import best.asoul.aircraft.handler.bullet.ShotToPlayerDirectionHandler;
import best.asoul.aircraft.handler.bullet.StraightWithReflectHandler;

/**
 * @Description 白色二级敌机
 * @Author Enchantedyou
 * @Date 2021/12/10-23:49
 */
public class EnemyWhiteLevel2 extends EnemyAircraft {

	public EnemyWhiteLevel2(int maxHealthPoint) {
		super(ResourceConst.ENEMY_LEVEL2_WHITE_AIRCRAFT, new LongOvalBullet(), maxHealthPoint);
		flyingConfig.setMaxEnemyY(250);
	}

	public EnemyWhiteLevel2() {
		this(20000);
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		shotChain.append(new StraightWithReflectHandler(true, 20, 6000L, 30L, -30D, -60D, -90D, -120D, -150D));
		shotChain.append(new ShotToPlayerDirectionHandler(25, 40, 12, 0, 1000L));
	}
}
