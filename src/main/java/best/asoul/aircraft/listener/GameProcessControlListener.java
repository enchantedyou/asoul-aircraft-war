package best.asoul.aircraft.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.entity.GameSession;
import best.asoul.aircraft.entity.GameStatus;
import best.asoul.aircraft.factory.JFrameFactory;
import best.asoul.aircraft.thread.base.AsoulThreadPoolHelper;
import best.asoul.aircraft.util.SoundUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 游戏流程控制监听
 * @Author Enchantedyou
 * @Date 2021年11月27日-13:15
 */
@Slf4j
public class GameProcessControlListener implements KeyListener {

	private JFrame currentJFrame;
	private GameSession gameSession;

	public GameProcessControlListener(JFrame currentJFrame) {
		this.currentJFrame = currentJFrame;
		gameSession = GameContext.getGameSession();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// 忽略
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			log.info("退出游戏");
			System.exit(GlobalConst.EXIT_SUCCESS_CODE);
		} else if (e.getKeyCode() != KeyEvent.VK_SPACE) {
			return;
		}
		final GameStatus gameStatus = gameSession.getGameStatus();
		// 游戏开始
		if (gameStatus == GameStatus.NOT_STARTED) {
			JFrameFactory.createStage1JFrame(SoundUtil::loopAdventureBgm);
			currentJFrame.dispose();
			GameContext.switchGameStatus(GameStatus.RUNNING);
			// 强制鼠标到玩家战机所在位置
			GameContext.moveMouseToPlayerPos();
		}
		// 游戏暂停
		else if (gameStatus == GameStatus.RUNNING) {
			// 特效状态保存
			GameContext.getPlayerAircraft().saveHoldBoost();
			AsoulThreadPoolHelper.gamePauseOrResume();
			GameContext.switchGameStatus(GameStatus.PAUSE);
		}
		// 游戏恢复
		else if (gameStatus == GameStatus.PAUSE) {
			// 特效状态恢复
			GameContext.getPlayerAircraft().recoveryHoldBoost();
			AsoulThreadPoolHelper.gamePauseOrResume();
			GameContext.switchGameStatus(GameStatus.RUNNING);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// 忽略
	}
}
