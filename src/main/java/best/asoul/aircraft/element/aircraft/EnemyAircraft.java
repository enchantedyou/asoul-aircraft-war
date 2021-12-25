package best.asoul.aircraft.element.aircraft;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.config.aircraft.EnemyAircraftConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.BossType;
import best.asoul.aircraft.handler.bullet.ShotToPlayerDirectionHandler;
import best.asoul.aircraft.util.AsoulUtil;

/**
 * @Description 抽象的敌机
 * @Author Enchantedyou
 * @Date 2021/11/20-16:28
 */
public abstract class EnemyAircraft extends Aircraft {

	/** boss类型 **/
	private BossType bossType;

	protected EnemyAircraft(String imageKey, Bullet bullet) {
		super(imageKey, bullet);
		initBaseInfo(10000);
	}

	@Override
	public boolean bulletLevelUp() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void determineShotChain(ShotChain shotChain) {
		shotChain.append(new ShotToPlayerDirectionHandler(30, 30, 18, 0, 500L));
	}

	protected EnemyAircraft(String imageKey, Bullet bullet, int maxHealthPoint) {
		super(imageKey, bullet);
		initBaseInfo(maxHealthPoint);
	}

	private void initBaseInfo(int maxHealthPoint) {
		super.flyingConfig = new EnemyAircraftConfig();
		initHealthPoint(maxHealthPoint > 0 ? maxHealthPoint : 20000);
		super.camp = AircraftCamp.ENEMY;

		// 指定战机判定范围
		if (ResourceConst.ENEMY_LEVEL1_BLUE_AIRCRAFT.equals(imageKey)
				|| ResourceConst.ENEMY_LEVEL1_RED_AIRCRAFT.equals(imageKey)) {
			flyingConfig.determineSize(140, 95);
		} else if (ResourceConst.ENEMY_LEVEL2_BLUE_AIRCRAFT.equals(imageKey)) {
			flyingConfig.determineSize(305, 220);
		} else if (ResourceConst.ENEMY_LEVEL2_PURPLE_AIRCRAFT.equals(imageKey)) {
			flyingConfig.determineSize(140, 135);
		} else if (ResourceConst.ENEMY_LEVEL2_WHITE_AIRCRAFT.equals(imageKey)) {
			flyingConfig.determineSize(245, 300);
		} else if (ResourceConst.BOSS_TOOLS_PEOPLE.equals(imageKey)) {
			flyingConfig.determineSize(300, 300);
		}
	}

	@Override
	public void afterDead() {
		// 飞机爆炸的动画和音效
		explodeAfterDead();
		// 敌机阵亡后概率出现增益效果
		AsoulUtil.randCreateDriftBoost(this);
	}

	public BossType getBossType() {
		return bossType;
	}

	public void setBossType(BossType bossType) {
		this.bossType = bossType;
	}
}