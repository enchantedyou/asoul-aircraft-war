package best.asoul.aircraft.component.stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.*;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.define.DefaultStageDefine;
import best.asoul.aircraft.define.StageDefine;
import best.asoul.aircraft.element.aircraft.AsoulAircraft;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.element.boost.DriftBoot;
import best.asoul.aircraft.element.bullet.AvaBullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.factory.AnimationResourceFactory;
import best.asoul.aircraft.factory.ImageResourceFactory;
import best.asoul.aircraft.invoker.CollideAfterInvoker;
import best.asoul.aircraft.listener.AircraftMouseListener;
import best.asoul.aircraft.thread.ScreenRefreshTask;
import best.asoul.aircraft.thread.base.AsoulThreadPoolHelper;
import best.asoul.aircraft.util.JFrameUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 第一关
 * @Author Enchantedyou
 * @Date 2021年11月20日-01:04
 */
@Slf4j
public class StagePanel extends JPanel {

	private transient StageDefine stageDefine = new DefaultStageDefine();

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
		AsoulThreadPoolHelper.submitNoPauseTask(new ScreenRefreshTask(this));
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
		// 玩家的战机放到后面再画以保证图层在最上面
		final List<Aircraft> playerList = stageDefine.getPlayerList();
		drawAircraftList(g, enemyList, toBeRemovedAircraftList);
		drawAircraftList(g, playerList, toBeRemovedAircraftList);

		// 碰撞检测
		collisionCheck(enemyList, playerList);
		collisionCheck(toBeRemovedAircraftList, playerList);
		// 绘制动画效果
		playAnimation(g);
		// 移除过期的增益效果
		removeExpiredBoost(playerList);
		// 移除已死亡且现存子弹数为0的战机
		toBeRemovedAircraftList.removeIf(e -> e.getShotList().isEmpty());
		/** 缓冲区绘制结束 **/
		graphics.drawImage(bufferedImage, 0, 0, this);
	}

	private void removeExpiredBoost(List<Aircraft> playerList) {
		for (Aircraft player : playerList) {
			player.removeExpiredBoost();
		}
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

	private void collisionCheck(List<Aircraft> enemyList, List<Aircraft> playerList) {
		for (Aircraft player : playerList) {
			final FlyingConfig playerConfig = player.getConfig();
			final List<Bullet> playerShotList = player.getShotList();

			// 增强效果拾取检查
			checkBoostTrigger(player, playerConfig);
			for (Aircraft enemy : enemyList) {
				final FlyingConfig enemyConfig = enemy.getConfig();
				final List<Bullet> enemyShotList = enemy.getShotList();

				if (!enemy.isDead()) {
					// 飞机碰撞->玩家扣血
					doCollisionCheck(player.getImage(), enemy.getImage(), playerConfig, enemyConfig,
							() -> player.deductHealthPointCollision(GlobalConst.COLLISION_DEDUCT_NORMAL));
					for (Bullet playerBullet : playerShotList) {
						// 玩家子弹击中敌机
						doCollisionCheck(playerBullet.getImage(), enemy.getImage(), playerBullet.getConfig(),
								enemyConfig,
								() -> {
									// 敌机扣血
									enemy.deductHealthPoint(playerBullet);
									// 移除子弹
									playerShotList.remove(playerBullet);
								});
					}
				}
				for (Bullet enemyBullet : enemyShotList) {
					// 敌机子弹击中玩家
					doCollisionCheck(player.getImage(), enemyBullet.getImage(), playerConfig, enemyBullet.getConfig(),
							() -> {
								// 玩家扣血
								player.deductHealthPoint(enemyBullet);
								// 移除子弹
								enemyShotList.remove(enemyBullet);
							});
				}
			}
		}
	}

	private void checkBoostTrigger(Aircraft player, FlyingConfig playerConfig) {
		for (DriftBoot driftBoot : stageDefine.getDriftBootList()) {
			// 玩家拾取到增益效果
			doCollisionCheck(player.getImage(), driftBoot.getImage(), playerConfig, driftBoot.getConfig(),
					() -> driftBoot.boostTrigger(player));
		}
	}

	private void doCollisionCheck(BufferedImage playerImage, BufferedImage enemyImage, FlyingConfig playerConfig,
			FlyingConfig enemyConfig,
			CollideAfterInvoker collideAfterInvoker) {
		// 计算真正的需要判定的矩形x、y轴坐标，图片的大小不等于实际上想要的子弹、飞机大小
		int playerX = playerConfig.getX() + playerImage.getWidth() / 2 - playerConfig.getWidth() / 2;
		int playerY = playerConfig.getY() + playerImage.getHeight() / 2 - playerConfig.getHeight() / 2;
		int enemyX = enemyConfig.getX() + enemyImage.getWidth() / 2 - enemyConfig.getWidth() / 2;
		int enemyY = enemyConfig.getY() + enemyImage.getHeight() / 2 - enemyConfig.getHeight() / 2;

		// 这个地方没考虑到子弹倾角的问题，但是不知道咋改，所以摆烂了
		final Rectangle playerRectangle = new Rectangle(playerX, playerY, playerConfig.getWidth(),
				playerConfig.getHeight());
		final Rectangle enemyRectangle = new Rectangle(enemyX, enemyY, enemyConfig.getWidth(), enemyConfig.getHeight());

		if (playerRectangle.intersects(enemyRectangle) && collideAfterInvoker != null) {
			collideAfterInvoker.invoke();
		}
	}

	private void drawAircraftList(Graphics2D g, List<Aircraft> aircraftList, List<Aircraft> toBeMovedAircraftList) {
		for (Aircraft aircraft : aircraftList) {
			if (aircraft.isDead() && !toBeMovedAircraftList.contains(aircraft)) {
				aircraftList.remove(aircraft);
				toBeMovedAircraftList.add(aircraft);
				// 飞机爆炸的动画和音效
				final AnimationEffectPlayer effectPlayer = AnimationResourceFactory
						.buildAnimationPlayer(AnimationType.AIRCRAFT_EXPLODE, aircraft);
				stageDefine.getEffectList().add(effectPlayer);
				// 阵亡后处理
				aircraft.afterDead();
			} else {
				// 绘制血条
				if (aircraft.getCamp() == AircraftCamp.ASOUL) {
					JFrameUtil.drawBlood(g, 60, 28, aircraft);
				} else {
					JFrameUtil.drawBlood(g, 0, aircraft.getImage().getHeight() + 10, aircraft);
				}
			}
			doDrawAircraft(g, aircraft);
		}

		// 绘制待移除战机列表里面的子弹
		for (Aircraft aircraft : toBeMovedAircraftList) {
			doDrawAircraft(g, aircraft);
		}
	}

	private void doDrawAircraft(Graphics2D g, Aircraft aircraft) {
		// 绘制战机
		JFrameUtil.drawAircraft(g, aircraft);
		// 绘制子弹
		for (Bullet bullet : aircraft.getShotList()) {
			JFrameUtil.drawBullet(g, bullet, aircraft.getCamp());
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
