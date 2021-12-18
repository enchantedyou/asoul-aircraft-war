package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.bullet.MidArcBullet;
import best.asoul.aircraft.handler.bullet.ShotToPlayerDirectionHandler;

/**
 * @Description 红色一级敌机
 * @Author Enchantedyou
 * @Date 2021/12/10-23:46
 */
public class EnemyRedLevel1 extends EnemyAircraft {

	public EnemyRedLevel1(int maxHealthPoint) {
		super(ResourceConst.ENEMY_LEVEL1_RED_AIRCRAFT, new MidArcBullet(), maxHealthPoint);
		flyingConfig.setMaxEnemyY(300);
	}

	public EnemyRedLevel1() {
		this(5000);
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		shotChain.append(new ShotToPlayerDirectionHandler(20, 100, 10, 0, 1000L));
	}
}
