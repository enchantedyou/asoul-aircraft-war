package best.asoul.aircraft.define;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import best.asoul.aircraft.chain.AircraftCreateChain;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.boost.DriftBoot;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.handler.aircraft.DefaultAircraftCreateHandler;
import best.asoul.aircraft.thread.AircraftCreateTask;
import best.asoul.aircraft.thread.BoostMoveTask;
import best.asoul.aircraft.thread.base.AsoulThreadPoolHelper;

/**
 * @Description 关卡定义
 * @Author Enchantedyou
 * @Date 2021年11月23日-22:48
 */
public abstract class StageDefine {

	/** 1P **/
	protected Aircraft player1;
	/** 2P **/
	protected Aircraft player2;
	/** 敌机列表：在战机生成线程真正执行之前这里面都是空的 **/
	protected List<Aircraft> enemyList = new CopyOnWriteArrayList<>();
	/** 玩家列表 **/
	protected List<Aircraft> playerList = new CopyOnWriteArrayList<>();
	/** 待移除的战机列表 **/
	protected List<Aircraft> toBeRemovedAircraftList = new CopyOnWriteArrayList<>();
	/** 背景移动的Y轴坐标 **/
	protected int backgroundImageY = 0;
	/** 动画播放器列表 **/
	protected List<AnimationEffectPlayer> effectList = new CopyOnWriteArrayList<>();
	/** 游离的增益效果列表 **/
	protected List<DriftBoot> driftBootList = new CopyOnWriteArrayList<>();
	/** 敌机生成链 **/
	protected AircraftCreateChain enemyCreateChain = new AircraftCreateChain();

	/**
	 * @Description 定义敌机生成处理器
	 * @Author Enchantedyou
	 * @Date 2021/11/23-23:22
	 */
	protected abstract void enemyCreateHandler();

	/**
	 * @Description 关卡开始（双人）
	 * @Author Enchantedyou
	 * @Date 2021/11/23-23:32
	 * @param player1
	 * @param player2
	 */
	public void startStage(Aircraft player1, Aircraft player2) {
		// 指定当前游戏关卡
		GameContext.determineStageDefine(this);
		// 创建玩家战机（必须在敌机生成前）
		createPlayer(player1, player2);
		// 获取生成敌机的处理器
		enemyCreateHandler();
		// 提交敌机生成的线程
		AsoulThreadPoolHelper.submitGameTask(new AircraftCreateTask(enemyCreateChain));
		// 提交增益效果移动的线程
		AsoulThreadPoolHelper.submitGameTask(new BoostMoveTask(driftBootList));
	}

	/**
	 * @Description 关卡开始（单人）
	 * @Author Enchantedyou
	 * @Date 2021/11/23-23:42
	 * @param player
	 */
	public void startStage(Aircraft player) {
		startStage(player, null);
	}

	/**
	 * @Description 创建玩家战机（双人）
	 * @Author Enchantedyou
	 * @Date 2021/11/23-22:58
	 * @param player1
	 * @param player2
	 */
	protected final void createPlayer(Aircraft player1, Aircraft player2) {
		this.player1 = player1;
		this.player2 = player2;

		AircraftCreateChain playerCreateChain = new AircraftCreateChain();
		// p1当做当前玩家
		GameContext.determineCurrentPlayer(player1);
		// 生成p1
		playerCreateChain.append(new DefaultAircraftCreateHandler(player1, playerList));
		// 生成p2
		if (player2 != null) {
			playerCreateChain.append(new DefaultAircraftCreateHandler(player2, playerList));
		}
		AsoulThreadPoolHelper.submitNoPauseTask(new AircraftCreateTask(playerCreateChain));
	}

	/**
	 * @Description 创建玩家战机（单人）
	 * @Author Enchantedyou
	 * @Date 2021/11/23-23:00
	 * @param player1
	 */
	protected final void createPlayer(Aircraft player1) {
		createPlayer(player1, null);
	}

	public Aircraft getPlayer1() {
		return player1;
	}

	public Aircraft getPlayer2() {
		return player2;
	}

	public List<Aircraft> getEnemyList() {
		return enemyList;
	}

	public List<Aircraft> getPlayerList() {
		return playerList;
	}

	public void setBackgroundImageY(int backgroundImageY) {
		this.backgroundImageY = backgroundImageY;
	}

	public int getBackgroundImageY() {
		return backgroundImageY;
	}

	public List<AnimationEffectPlayer> getEffectList() {
		return effectList;
	}

	public List<DriftBoot> getDriftBootList() {
		return driftBootList;
	}

	public AircraftCreateChain getEnemyCreateChain() {
		return enemyCreateChain;
	}

	public List<Aircraft> getToBeRemovedAircraftList() {
		return toBeRemovedAircraftList;
	}
}
