package best.asoul.aircraft.define;

import best.asoul.aircraft.element.aircraft.*;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.handler.aircraft.EnemyAircraftCreateHandler;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 关卡1设定
 * @Author Enchantedyou
 * @Date 2021年11月22日-22:31
 */
public final class DefaultStageDefine extends StageDefine {

	@Override
	protected void enemyCreateHandler() {
		// 创建模板飞机
		EnemyBlueLevel1 blueLevel1 = new EnemyBlueLevel1();
		EnemyRedLevel1 redLevel1 = new EnemyRedLevel1();
		EnemyBlueLevel2 blueLevel2 = new EnemyBlueLevel2();
		EnemyPurpleLevel2 purpleLevel2 = new EnemyPurpleLevel2();
		EnemyWhiteLevel2 whiteLevel2 = new EnemyWhiteLevel2();
		// 第一关
		createStage1(blueLevel1, redLevel1, blueLevel2, purpleLevel2, whiteLevel2);
	}

	private void createStage1(EnemyBlueLevel1 blueLevel1, EnemyRedLevel1 redLevel1, EnemyBlueLevel2 blueLevel2,
			EnemyPurpleLevel2 purpleLevel2, EnemyWhiteLevel2 whiteLevel2) {
		// 5蓝lv1
		createEnemy(blueLevel1, null, 5);
		// 5红lv1
		createEnemy(redLevel1, null, 5);
		// 2蓝lv1，中紫lv2
		createEnemy(blueLevel1, purpleLevel2, 3);
		// 2红lv1，中白lv2
		createEnemy(redLevel1, whiteLevel2, 3);
		// 4蓝lv1，中紫lv2
		createEnemy(blueLevel1, purpleLevel2, 5);
		// 4红lv1，中蓝lv2
		createEnemy(redLevel1, blueLevel2, 5);
		// 2蓝lv2，中白lv2
		createEnemy(blueLevel2, whiteLevel2, 3);
		// 2紫lv2，中白lv2
		createEnemy(purpleLevel2, whiteLevel2, 3);
		// 2白lv2，中紫lv2
		createEnemy(whiteLevel2, purpleLevel2, 3);
		// 4蓝lv2，1白lv2
		createEnemy(blueLevel2, whiteLevel2, 5);
		// boss
		createBoss(new BossToolsPeople());
	}

	private void createEnemy(EnemyAircraft enemyAircraft, EnemyAircraft mid, int count) {
		EnemyAircraftCreateHandler handler = new EnemyAircraftCreateHandler(count, enemyAircraft,
				mid, enemyList);
		handler.setBeforeNextHandleInvoker(this::awaitAllEnemyDestroyed);
		enemyCreateChain.append(handler);
	}

	private void createBoss(EnemyAircraft boss) {
		boss.setCamp(AircraftCamp.BOSS);
		EnemyAircraftCreateHandler handler = new EnemyAircraftCreateHandler(1, boss, null, enemyList);
		handler.setBeforeNextHandleInvoker(this::awaitAllEnemyDestroyed);
		enemyCreateChain.append(handler);
	}

	/**
	 * @Description 等待所有敌机阵亡
	 * @Author Enchantedyou
	 * @Date 2021/12/5-17:33
	 */
	private void awaitAllEnemyDestroyed() {
		while (!enemyList.isEmpty()) {
			AsoulUtil.pause(200L);
		}
	}
}
