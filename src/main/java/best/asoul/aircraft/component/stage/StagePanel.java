package best.asoul.aircraft.component.stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.*;

import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.define.DefaultStageDefine;
import best.asoul.aircraft.define.StageDefine;
import best.asoul.aircraft.element.aircraft.AsoulAircraft;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.element.bullet.AvaBullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.factory.ImageResourceFactory;
import best.asoul.aircraft.listener.AircraftMouseListener;
import best.asoul.aircraft.thread.ScreenRefreshTask;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import best.asoul.aircraft.util.JFrameUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 第一关
 * @Author Enchantedyou
 * @Date 2021年11月20日-01:04
 */
@Slf4j
public class StagePanel extends JPanel {

	private final transient StageDefine stageDefine = new DefaultStageDefine();

	public StagePanel() {
		// 生成玩家战机
		AsoulAircraft avaAircraft = new AsoulAircraft(ResourceConst.AVA_AIRCRAFT, new AvaBullet());
		stageDefine.startStage(avaAircraft);
		final Aircraft player = stageDefine.getPlayer1();

		this.setDoubleBuffered(true);
		final AircraftMouseListener mouseListener = new AircraftMouseListener(player);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
		// 提交画面刷新的线程
		AsoulThreadHelper.submitTask(new ScreenRefreshTask(this));
	}

	@Override
	public void paint(Graphics graphics) {
		final BufferedImage bufferedImage = new BufferedImage(GlobalConfig.SCREEN_WIDTH,
				GlobalConfig.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = bufferedImage.createGraphics();

		/** 缓冲区绘制开始 **/
		// 绘制主界面滚动背景
		backgroundImageSlide(g);
		// 绘制玩家血量面板
		JFrameUtil.drawImageXCenter(g, ResourceConst.PLAYER_BLOOD_BASE, 0);
		// 待移除战机列表
		final List<Aircraft> toBeRemovedAircraftList = stageDefine.getToBeRemovedAircraftList();
		final List<Aircraft> enemyList = stageDefine.getEnemyList();
		final List<Aircraft> playerList = stageDefine.getPlayerList();

		//绘制战机和子弹，优先级（优先越低的越先画）：玩家战机->敌方子弹->玩家子弹->敌方战机
		drawAircraft(g, enemyList);
		drawBullet(g, playerList);
		drawBullet(g, enemyList);
		drawAircraft(g, playerList);
		drawBullet(g, toBeRemovedAircraftList);

		// 绘制动画效果
		playAnimation(g);
		/** 缓冲区绘制结束 **/
		graphics.drawImage(bufferedImage, 0, 0, this);
	}

	private void playAnimation(Graphics2D g) {
		for (AnimationEffectPlayer effect : stageDefine.getEffectList()) {
			if (effect.isPlayFinish()) {
				stageDefine.getEffectList().remove(effect);
			} else {
				effect.play(g);
			}
		}
	}

	private void drawAircraft(Graphics2D g, List<Aircraft> aircraftList) {
		for (Aircraft aircraft : aircraftList) {
			// 绘制血条
			if (aircraft.getCamp() == AircraftCamp.ASOUL) {
				JFrameUtil.drawBlood(g, 60, 28, aircraft);
			} else {
				JFrameUtil.drawBlood(g, 0, aircraft.getImage().getHeight() + 10, aircraft);
			}
			// 绘制战机
			JFrameUtil.drawAircraft(g, aircraft);
		}
	}

	private void drawBullet(Graphics2D g, List<Aircraft> aircraftList) {
		for (Aircraft aircraft : aircraftList) {
			for (Bullet bullet : aircraft.getShotList()) {
				JFrameUtil.drawBullet(g, bullet, aircraft);
			}
		}
	}

	private void backgroundImageSlide(Graphics2D g) {
		final int slideSpeed = -3;
		final BufferedImage image = ImageResourceFactory.getImage(ResourceConst.GAME_BACKGROUND);
		int backgroundImageY = stageDefine.getBackgroundImageY();

		if (backgroundImageY >= 0 && backgroundImageY + GlobalConfig.SCREEN_HEIGHT <= image.getHeight()) {
			final BufferedImage subImage = image.getSubimage(0, backgroundImageY, image.getWidth(),
					GlobalConfig.SCREEN_HEIGHT);
			g.drawImage(subImage, 0, 0, getSize().width, getSize().height, this);
		} else if (backgroundImageY < 0) {
			final BufferedImage subImage = image.getSubimage(0, image.getHeight() + backgroundImageY, image.getWidth(),
					-backgroundImageY);
			g.drawImage(subImage, 0, 0, GlobalConfig.SCREEN_WIDTH, -backgroundImageY, this);
			g.drawImage(image, 0, -backgroundImageY, getSize().width, getSize().height, this);

			if (-backgroundImageY > GlobalConfig.SCREEN_HEIGHT) {
				backgroundImageY += image.getHeight();
			}
		}

		backgroundImageY += slideSpeed;
		if (backgroundImageY < -GlobalConfig.SCREEN_HEIGHT) {
			backgroundImageY = slideSpeed;
		}
		stageDefine.setBackgroundImageY(backgroundImageY);
	}
}
