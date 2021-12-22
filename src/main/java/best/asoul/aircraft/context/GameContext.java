package best.asoul.aircraft.context;

import java.awt.image.BufferedImage;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.define.StageDefine;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.GameSession;
import best.asoul.aircraft.entity.GameStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 游戏上下文
 * @Author Enchantedyou
 * @Date 2021年11月27日-13:24
 */
@Slf4j
public class GameContext {

	private GameContext() {
	}

	private static final GameSession GAME_SESSION = new GameSession();

	/**
	 * @Description 获取当前游戏会话
	 * @Author Enchantedyou
	 * @Date 2021/11/27-13:29
	 * @return best.asoul.aircraft.entity.GameSession
	 */
	public static GameSession getGameSession() {
		return GAME_SESSION;
	}

	/**
	 * @Description 切换游戏状态
	 * @Author Enchantedyou
	 * @Date 2021/11/27-13:28
	 * @param gameStatus
	 */
	public static synchronized void switchGameStatus(GameStatus gameStatus) {
		GAME_SESSION.setGameStatus(gameStatus);
		log.info("游戏状态切换为：{}", gameStatus);
	}

	/**
	 * @Description 确定当前玩家
	 * @Author Enchantedyou
	 * @Date 2021/11/29-22:15
	 * @param player
	 */
	public static synchronized void determineCurrentPlayer(Aircraft player) {
		GAME_SESSION.setPlayer(player);
	}

	/**
	 * @Description 将鼠标移动至玩家战机所在位置
	 * @Author Enchantedyou
	 * @Date 2021/11/29-22:17
	 */
	public static void moveMouseToPlayerPos() {
		final FlyingConfig flyingConfig = GAME_SESSION.getPlayer().getConfig();
		final BufferedImage image = GAME_SESSION.getPlayer().getImage();
		// 位置修正
		GAME_SESSION.getRobot().mouseMove(flyingConfig.getX() + (image.getWidth() / 2),
				flyingConfig.getY() + (image.getHeight() / 2));
	}

	/**
	 * @Description 获取当前玩家的战机
	 * @Author Enchantedyou
	 * @Date 2021/11/29-22:45
	 * @return best.asoul.aircraft.element.base.Aircraft
	 */
	public static Aircraft getPlayerAircraft() {
		return GAME_SESSION.getPlayer();
	}

	/**
	 * @Description 设置当前关卡定义
	 * @Author Enchantedyou
	 * @Date 2021/12/4-13:59
	 * @param stageDefine
	 */
	public static synchronized void determineStageDefine(StageDefine stageDefine) {
		GAME_SESSION.setStageDefine(stageDefine);
	}

	/**
	 * @Description 获取当前关卡定义
	 * @Author Enchantedyou
	 * @Date 2021/12/4-14:00
	 * @return best.asoul.aircraft.define.StageDefine
	 */
	public static StageDefine getStageDefine() {
		return GAME_SESSION.getStageDefine();
	}
}
