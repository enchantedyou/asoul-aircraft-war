package best.asoul.aircraft.entity;

import java.awt.*;

import best.asoul.aircraft.define.StageDefine;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.exception.AsoulException;

/**
 * @Description 游戏当前会话，记录实时状态
 * @Author Enchantedyou
 * @Date 2021年11月27日-13:26
 */
public class GameSession {

	/** 游戏状态 **/
	private GameStatus gameStatus = GameStatus.NOT_STARTED;
	/** 当前玩家 **/
	private Aircraft player;
	/** 当前机器人 **/
	private Robot robot;
	/** 当前关卡 **/
	private StageDefine stageDefine;

	public GameSession() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new AsoulException("robot初始化失败");
		}
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Aircraft getPlayer() {
		return player;
	}

	public void setPlayer(Aircraft player) {
		this.player = player;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	public StageDefine getStageDefine() {
		return stageDefine;
	}

	public void setStageDefine(StageDefine stageDefine) {
		this.stageDefine = stageDefine;
	}
}
