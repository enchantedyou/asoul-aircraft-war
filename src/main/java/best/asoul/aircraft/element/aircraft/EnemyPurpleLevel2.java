package best.asoul.aircraft.element.aircraft;

import java.util.ArrayList;
import java.util.List;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.bullet.LittleBallBullet;
import best.asoul.aircraft.handler.bullet.StraightWithReflectHandler;
import best.asoul.aircraft.handler.bullet.WhirlpoolSpreadShotHandler;

/**
 * @Description 紫色二级敌机
 * @Author Enchantedyou
 * @Date 2021/12/10-23:50
 */
public class EnemyPurpleLevel2 extends EnemyAircraft {

	public EnemyPurpleLevel2(int maxHealthPoint) {
		super(ResourceConst.ENEMY_LEVEL2_PURPLE_AIRCRAFT, new LittleBallBullet(), maxHealthPoint);
		flyingConfig.setMaxEnemyY(250);
		bullet.getConfig().setMoveInterval(24);
	}

	public EnemyPurpleLevel2() {
		this(20000);
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		shotChain.append(new WhirlpoolSpreadShotHandler(20, 3, 1000L, true, 8));
		// 设置弧形发射的角度
		List<Double> degreesList = new ArrayList<>();
		for (double d = -150D; d <= -30D; d += 8D) {
			degreesList.add(d);
		}
		shotChain.append(new StraightWithReflectHandler(false, 12, 0L, 800L, degreesList.toArray(new Double[] {})));
	}
}
