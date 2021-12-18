package best.asoul.aircraft.element.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import best.asoul.aircraft.chain.ShotChain;
import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.config.aircraft.AsoulAircraftConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.element.boost.HoldBoost;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.BoostType;
import best.asoul.aircraft.entity.HpCalcMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 战机
 * @Author Enchantedyou
 * @Date 2021年11月20日-00:40
 */
@Slf4j
public abstract class Aircraft extends Flying implements Serializable {

	/** 当前生命值 **/
	protected int healthPoint;
	/** 生命值上限 **/
	protected int maxHealthPoint;
	/** 子弹 **/
	protected Bullet bullet;
	/** 战机阵营 **/
	protected AircraftCamp camp;
	/** 上次和敌机碰撞时间 **/
	protected long lastCollisionTime = 0L;
	/** 射击的子弹列表 **/
	protected CopyOnWriteArrayList<Bullet> shotList = new CopyOnWriteArrayList<>();
	/** 当前子弹等级 **/
	protected AtomicInteger bulletLevel = new AtomicInteger(1);
	/** 持有增益效果列表 **/
	protected transient List<HoldBoost> holdBoostList = new CopyOnWriteArrayList<>();
	/** 射击链 **/
	private ShotChain shotChain = new ShotChain(this);
	/** 觉醒级别 **/
	private AtomicInteger awakeLevel = new AtomicInteger(0);

	protected Aircraft(String aircraftImage, Bullet bullet) {
		super(aircraftImage, new AsoulAircraftConfig());
		this.bullet = bullet;
		determineShotChain(shotChain);
	}

	/**
	 * @Description 子弹升级
	 * @Author Enchantedyou
	 * @Date 2021/11/28-21:36
	 */
	public abstract boolean bulletLevelUp();

	/**
	 * @Description 指定射击链
	 * @Author Enchantedyou
	 * @Date 2021/12/6-21:18
	 * @param shotChain
	 */
	public abstract void determineShotChain(ShotChain shotChain);

	/**
	 * @Description 暴走结束
	 * @Author Enchantedyou
	 * @Date 2021/11/28-21:48
	 */
	public void endEnergyRestored() {
		// 子弹恢复为暴走前的等级
		bulletRecover();
		if (bulletLevel.get() == GlobalConst.ENERGY_RESTORED_LEVEL) {
			bullet.switchLevel(bulletLevel.decrementAndGet());
		}
	}

	/**
	 * @Description 判断是否暴走
	 * @Author Enchantedyou
	 * @Date 2021/12/3-23:07
	 * @return boolean
	 */
	public boolean isEnergyRestored() {
		return bulletLevel.get() >= GlobalConst.ENERGY_RESTORED_LEVEL;
	}

	/**
	 * @Description 子弹增强
	 * @Author Enchantedyou
	 * @Date 2021/11/29-21:42
	 */
	protected synchronized void bulletBoost() {
		final FlyingConfig bulletConfig = bullet.getConfig();
		bulletConfig.setMoveInterval(bulletConfig.getMoveInterval() - 4);
		bulletConfig.setCreateInterval(bulletConfig.getCreateInterval() - 100);
		bulletConfig.increaseSpeed(+25D);
		// 子弹攻击力减半
		bullet.setAttack(bullet.getAttack() / 2);
	}

	/**
	 * @Description 子弹恢复
	 * @Author Enchantedyou
	 * @Date 2021/11/29-21:43
	 */
	protected synchronized void bulletRecover() {
		final FlyingConfig bulletConfig = bullet.getConfig();
		bulletConfig.setMoveInterval(bulletConfig.getMoveInterval() + 4);
		bulletConfig.setCreateInterval(bulletConfig.getCreateInterval() + 100);
		bulletConfig.increaseSpeed(-25D);
		// 子弹攻击力恢复
		bullet.setAttack(bullet.getAttack() * 2);
	}

	/**
	 * @Description 获取当前血量百分比
	 * @Author Enchantedyou
	 * @Date 2021/12/4-23:30
	 * @return double
	 */
	public double getHealthPointPercent() {
		final BigDecimal p = BigDecimal.valueOf(this.healthPoint)
				.divide(BigDecimal.valueOf(this.maxHealthPoint), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal("100"));
		return Math.max(p.doubleValue(), 0D);
	}

	public Bullet getBullet() {
		return bullet;
	}

	public AircraftCamp getCamp() {
		return camp;
	}

	public int getBulletLevel() {
		return bulletLevel.get();
	}

	public void setCamp(AircraftCamp camp) {
		this.camp = camp;
	}

	public void setBullet(Bullet bullet) {
		this.bullet = bullet;
	}

	public List<Bullet> getShotList() {
		return shotList;
	}

	public ShotChain getShotChain() {
		return shotChain;
	}

	public int getAwakeLevel() {
		return awakeLevel.get();
	}

	/**
	 * @Description 觉醒升级
	 * @Author Enchantedyou
	 * @Date 2021/12/18-16:55
	 * @return int
	 */
	public int awakeLevelUp() {
		return awakeLevel.incrementAndGet();
	}

	/**
	 * @Description 初始化生命值
	 * @Author Enchantedyou
	 * @Date 2021/11/26-17:14
	 * @param maxHealthPoint
	 */
	public void initHealthPoint(int maxHealthPoint) {
		if (maxHealthPoint <= 0) {
			throw new IllegalArgumentException("血量上限非法");
		}
		this.maxHealthPoint = maxHealthPoint;
		healthPoint = maxHealthPoint;
	}

	/**
	 * @Description 百分比扣血
	 * @Author Enchantedyou
	 * @Date 2021/11/26-17:30
	 * @param percent
	 */
	public void deductHealthPointPercent(int percent) {
		final int maxPercent = checkPercent(percent);
		this.healthPoint -= (maxHealthPoint * percent / maxPercent);
	}

	private int checkPercent(int percent) {
		final int maxPercent = 100;
		if (percent <= 0 || percent > maxPercent) {
			throw new IllegalArgumentException("百分比非法");
		}
		return maxPercent;
	}

	/**
	 * @Description 飞机碰撞扣血
	 * @Author Enchantedyou
	 * @Date 2021/11/26-17:53
	 * @param deductMethod
	 * @param value
	 */
	public synchronized void deductHealthPointCollision(HpCalcMethod deductMethod, int value) {
		if (isAbsoluteDefense()) {
			return;
		}

		final long now = System.currentTimeMillis();
		// 防止频繁扣血
		if (now - lastCollisionTime <= GlobalConst.AIRCRAFT_COLLISION_INTERVAL) {
			return;
		}

		if (deductMethod == HpCalcMethod.PERCENT) {
			deductHealthPointPercent(value);
		} else if (deductMethod == HpCalcMethod.VALUE) {
			deductHealthPoint(value);
		}
		lastCollisionTime = now;
	}

	/**
	 * @Description 百分比增加血量
	 * @Author Enchantedyou
	 * @Date 2021/12/4-13:04
	 * @param percent
	 */
	public synchronized void increaseHealthPointPercent(int percent) {
		final int maxPercent = checkPercent(percent);
		final int incrementHp = maxHealthPoint * percent / maxPercent;
		final int originalHp = this.healthPoint;

		if (incrementHp + this.healthPoint > maxHealthPoint) {
			this.healthPoint = maxHealthPoint;
		} else {
			this.healthPoint += incrementHp;
		}
		if (originalHp != this.healthPoint) {
			log.debug("HP恢复：{}", this.healthPoint - originalHp);
		}
	}

	/**
	 * @Description 飞机碰撞百分比扣血
	 * @Author Enchantedyou
	 * @Date 2021/11/26-17:53
	 * @param percent
	 */
	public void deductHealthPointCollision(int percent) {
		deductHealthPointCollision(HpCalcMethod.PERCENT, percent);
	}

	/**
	 * @Description 按子弹实际伤害扣血
	 * @Author Enchantedyou
	 * @Date 2021/11/26-17:44
	 * @param bullet
	 */
	public synchronized void deductHealthPoint(Bullet bullet) {
		if (isAbsoluteDefense()) {
			return;
		}
		this.healthPoint -= bullet.getAttack();
	}

	/**
	 * @Description 按实际伤害扣血
	 * @Author Enchantedyou
	 * @Date 2021/11/26-17:52
	 * @param point
	 */
	public synchronized void deductHealthPoint(int point) {
		if (isAbsoluteDefense()) {
			return;
		}
		this.healthPoint -= point;
	}

	/**
	 * @Description 是否免疫伤害
	 * @Author Enchantedyou
	 * @Date 2021/12/4-13:48
	 * @return boolean
	 */
	private boolean isAbsoluteDefense() {
		if (camp != AircraftCamp.ASOUL) {
			return false;
		}

		for (HoldBoost holdBoost : holdBoostList) {
			if (holdBoost.getBoostType() == BoostType.ABSOLUTE_DEFENSE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @Description 判断战机是否死亡（血量归零或向下飞出屏幕外）
	 * @Author Enchantedyou
	 * @Date 2021/11/24-22:48
	 * @return boolean
	 */
	public boolean isDead() {
		if (getConfig().getY() >= GlobalConfig.SCREEN_HEIGHT || this.healthPoint <= 0) {
			// 2021年12月13日20:55:00 战机死了不再清除子弹
			return true;
		}
		return false;
	}

	/**
	 * @Description 战机阵亡后做些什么（音效、buff相关）
	 * @Author Enchantedyou
	 * @Date 2021/12/15-21:50
	 */
	public abstract void afterDead();

	/**
	 * @Description 赋予或延长持有特效
	 * @Author Enchantedyou
	 * @Date 2021/12/3-21:28
	 * @param holdBoost
	 */
	public void endowBoost(HoldBoost holdBoost, long duration) {
		if (null == holdBoost) {
			return;
		}

		boolean prolong = false;
		for (HoldBoost existedBoost : holdBoostList) {
			// 延长同类型的特效持续时间
			if (existedBoost.getBoostType().equals(holdBoost.getBoostType())) {
				existedBoost.specifyOrProlongExpireTime(duration);
				prolong = true;
				break;
			}
		}
		if (!prolong) {
			holdBoostList.add(holdBoost);
			// 渲染动画
			holdBoost.renderAnimation();
		}
	}

	/**
	 * @Description 保存当前战机的增益效果
	 * @Author Enchantedyou
	 * @Date 2021/12/4-0:19
	 */
	public void saveHoldBoost() {
		for (HoldBoost holdBoost : holdBoostList) {
			holdBoost.saveState();
		}
	}

	/**
	 * @Description 恢复当前战机的增益效果
	 * @Author Enchantedyou
	 * @Date 2021/12/4-0:19
	 */
	public void recoveryHoldBoost() {
		for (HoldBoost holdBoost : holdBoostList) {
			holdBoost.recoveryState();
		}
	}

	/**
	 * @Description 移除所有持有的过期特效（包括动画效果）
	 * @Author Enchantedyou
	 * @Date 2021/12/3-21:29
	 */
	public void removeExpiredBoost() {
		holdBoostList.removeIf(b -> b.expiredAndRemove(this));
	}

	@Override
	public String toString() {
		return "Aircraft{" +
				"healthPoint=" + healthPoint +
				", maxHealthPoint=" + maxHealthPoint +
				", bullet=" + bullet +
				", camp=" + camp +
				'}';
	}
}
