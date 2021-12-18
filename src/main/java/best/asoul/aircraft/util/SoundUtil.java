package best.asoul.aircraft.util;

import java.util.concurrent.atomic.AtomicReference;

import javax.sound.sampled.Clip;

import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.factory.SoundResourceFactory;

/**
 * @Description 音效工具类
 * @Author Enchantedyou
 * @Date 2021年11月26日-21:06
 */
public class SoundUtil {

	/** 背景音乐Clip对象集 **/
	private static final AtomicReference<Clip> LAST_BGM_CLIP = new AtomicReference<>();

	private SoundUtil() {
	}

	private static void switchBgm(String soundKey) {
		final Clip lastClip = LAST_BGM_CLIP.get();
		if (lastClip != null) {
			lastClip.stop();
		}

		final Clip nowClip = SoundResourceFactory.getAudioClip(soundKey);
		nowClip.loop(GlobalConst.BGM_LOOP_COUNT);
		while (!LAST_BGM_CLIP.compareAndSet(lastClip, nowClip)) {
		}
	}

	/**
	 * @Description 停止bgm
	 * @Author Enchantedyou
	 * @Date 2021/12/12-21:49
	 */
	public static void stopBgm() {
		final Clip clip = LAST_BGM_CLIP.getAndSet(null);
		if (clip != null) {
			clip.stop();
		}
	}

	/**
	 * @Description 循环播放小boss背景音乐
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:08
	 */
	public static void loopHalfwayBossBgm() {
		switchBgm(ResourceConst.HALFWAY_BOSS_BGM);
	}

	/**
	 * @Description 循环播放大boss背景音乐
	 * @Author Enchantedyou
	 * @Date 2021/11/27-12:22
	 */
	public static void loopFinalBossBgm() {
		switchBgm(ResourceConst.FINAL_BOSS_BGM);
	}

	/**
	 * @Description 循环播放闯关bgm
	 * @Author Enchantedyou
	 * @Date 2021/11/27-12:36
	 */
	public static void loopAdventureBgm() {
		switchBgm(ResourceConst.ADVENTURE_BGM);
	}

	/**
	 * @Description 播放主菜单BGM
	 * @Author Enchantedyou
	 * @Date 2021/11/27-11:31
	 */
	public static void loopMenuBgm() {
		switchBgm(ResourceConst.MENU_BGM);
	}

	/**
	 * @Description 播放暴走音效
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:09
	 */
	public static void playEnergyRestored() {
		SoundResourceFactory.getAudioClip(ResourceConst.ENERGY_RESTORED).start();
	}

	/**
	 * @Description 播放敌机爆炸音效
	 * @Author Enchantedyou
	 * @Date 2021/11/26-23:10
	 */
	public static void playEnemyExplode() {
		SoundResourceFactory.getAudioClip(ResourceConst.ENEMY_EXPLODE).start();
	}

	/**
	 * @Description 播放游戏准备音效
	 * @Author Enchantedyou
	 * @Date 2021/12/12-21:12
	 */
	public static void playGameReady() {
		SoundResourceFactory.getAudioClip(ResourceConst.GAME_READY).start();
	}

	/**
	 * @Description 播放boss出现前的警告音效
	 * @Author Enchantedyou
	 * @Date 2021/12/12-21:15
	 */
	public static void playBossWarning() {
		SoundResourceFactory.getAudioClip(ResourceConst.BOSS_WARNING).start();
	}

	/**
	 * @Description 向晚这把声音很大
	 * @Author Enchantedyou
	 * @Date 2021/12/15-23:08
	 */
	public static void playAvaRambo() {
		SoundResourceFactory.getAudioClip(ResourceConst.AVA_RAMBO).start();
	}

	/**
	 * @Description 向晚：怎么推流辣！
	 * @Author Enchantedyou
	 * @Date 2021/12/15-23:16
	 */
	public static void playAvaPushStream() {
		SoundResourceFactory.getAudioClip(ResourceConst.AVA_PUSH_STREAM).start();
	}
}
