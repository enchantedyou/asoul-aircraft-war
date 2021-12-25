package best.asoul.aircraft.thread;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.element.boost.DriftBoot;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.invoker.CollideAfterInvoker;
import best.asoul.aircraft.thread.base.AsoulThreadHelper;
import best.asoul.aircraft.util.AsoulUtil;
import best.asoul.aircraft.util.SoundUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 游戏监控线程
 * @Author Enchantedyou
 * @Date 2021年12月12日-20:49
 */
@Slf4j
public class GameMonitorTask implements Runnable {

	@Override
	public void run() {
		// 游戏线程暂停2秒钟
		SoundUtil.playGameReady();
		AsoulUtil.pause(2000L);
		SoundUtil.playAvaPushStream();
		// 计数完成，其他线程开始工作
		AsoulThreadHelper.finishReady();

		/** 游戏监控开始 **/
		AtomicLong lastClearClipTime = new AtomicLong(0L);
		final List<Aircraft> enemyList = GameContext.getStageDefine().getEnemyList();
		final List<Aircraft> playerList = GameContext.getStageDefine().getPlayerList();
		final List<Aircraft> toBeRemovedAircraftList = GameContext.getStageDefine().getToBeRemovedAircraftList();

		while (!Thread.currentThread().isInterrupted()) {
			// 碰撞检测
			collisionCheck(enemyList, playerList);
			collisionCheck(toBeRemovedAircraftList, playerList);
			// 移除过期的增益效果
			removeExpiredBoost(playerList);
			// 玩家、敌人战机阵亡判定
			dealAircraftDead(playerList, toBeRemovedAircraftList);
			dealAircraftDead(enemyList, toBeRemovedAircraftList);
			// 移除已死亡且现存子弹数为0的战机
			toBeRemovedAircraftList.removeIf(e -> e.getShotList().isEmpty());

			AsoulUtil.pause(20L);
			// 每3秒清理一次失效的声音切片
			clearInvalidClip(lastClearClipTime);
		}
		/** 游戏监控结束 **/
	}

	private void clearInvalidClip(AtomicLong lastClearClipTime) {
		final long now = System.currentTimeMillis();
		if ((now - lastClearClipTime.get()) >= 3000L) {
			// 定期清理失效的声音切片
			log.debug("清理失效的音效资源数：{}", SoundUtil.clearInvalidClip());
			// 直接set即可，无需做cas操作
			lastClearClipTime.set(now);
		}
	}

	private void dealAircraftDead(List<Aircraft> aircraftList, List<Aircraft> toBeRemovedAircraftList) {
		for (Aircraft aircraft : aircraftList) {
			if (aircraft.isDead() && !toBeRemovedAircraftList.contains(aircraft)) {
				// 阵亡后处理
				aircraft.afterDead();
				aircraftList.remove(aircraft);
				toBeRemovedAircraftList.add(aircraft);
			}
		}
	}

	private void removeExpiredBoost(List<Aircraft> playerList) {
		for (Aircraft player : playerList) {
			player.removeExpiredBoost();
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
				// 检查敌我战机碰撞以及玩家子弹命中
				checkCollisionAndPlayerShot(player, playerConfig, playerShotList, enemy, enemyConfig);

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

	private void checkCollisionAndPlayerShot(Aircraft player, FlyingConfig playerConfig, List<Bullet> playerShotList,
			Aircraft enemy, FlyingConfig enemyConfig) {
		if (!enemy.isDead()) {
			// 飞机碰撞->玩家扣血
			doCollisionCheck(player.getImage(), enemy.getImage(), playerConfig, enemyConfig,
					() -> player.deductHealthPointCollision(
							enemy.getCamp() == AircraftCamp.BOSS ? GlobalConst.COLLISION_DEDUCT_BOSS
									: GlobalConst.COLLISION_DEDUCT_NORMAL));
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
	}

	private void checkBoostTrigger(Aircraft player, FlyingConfig playerConfig) {
		for (DriftBoot driftBoot : GameContext.getStageDefine().getDriftBootList()) {
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
}
