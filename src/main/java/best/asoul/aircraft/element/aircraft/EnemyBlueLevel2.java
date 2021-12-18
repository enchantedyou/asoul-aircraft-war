package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.bullet.LittleRectangleBullet;
import best.asoul.aircraft.handler.bullet.WhirlpoolSpreadShotHandler;

/**
 * @Description 蓝色二级敌机
 * @Author Enchantedyou
 * @Date 2021/12/10-23:47
 */
public class EnemyBlueLevel2 extends EnemyAircraft {

	public EnemyBlueLevel2(int maxHealthPoint) {
		super(ResourceConst.ENEMY_LEVEL2_BLUE_AIRCRAFT, new LittleRectangleBullet(), maxHealthPoint);
		flyingConfig.setMaxEnemyY(250);
	}

	public EnemyBlueLevel2() {
		this(30000);
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		shotChain.append(new WhirlpoolSpreadShotHandler(15, 0, 1200L, false, 12));
	}
}
