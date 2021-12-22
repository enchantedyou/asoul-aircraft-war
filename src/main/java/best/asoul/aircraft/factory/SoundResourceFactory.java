package best.asoul.aircraft.factory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import best.asoul.aircraft.config.UserConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.exception.AsoulException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 音效资源工厂
 * @Author Enchantedyou
 * @Date 2021/11/26-20:58
 */
@Slf4j
public class SoundResourceFactory {

	private SoundResourceFactory() {
	}

	/** 音效资源 **/
	private static final Map<String, URL> AUDIO_MAP = new ConcurrentHashMap<>();

	/**
	 * @Description 获取音效资源文件
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:00
	 * @param soundKey
	 * @return java.net.URL
	 */
	public static Clip getAudioClip(String soundKey) {
		try {
			final URL url = AUDIO_MAP.get(soundKey);
			Clip audioClip = AudioSystem.getClip();
			audioClip.open(AudioSystem.getAudioInputStream(url));
			// 音量控制
			controlVolume(soundKey, audioClip);
			return audioClip;
		} catch (Exception e) {
			throw new AsoulException("音效资源加载失败：", e);
		}
	}

	/**
	 * @Description 填充音效资源
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:04
	 * @param file
	 */
	protected static void fillAudioResource(File file) {
		final String soundKey = file.getName().split("\\.")[0];
		log.info("加载音效资源：{}", soundKey);
		try {
			AUDIO_MAP.put(soundKey, file.toURI().toURL());
		} catch (IOException e) {
			log.error("音效资源加载失败：{}", soundKey);
		}
	}

	private static void controlVolume(String soundKey, Clip audioClip) {
		FloatControl control = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
		final float range = control.getMinimum() + GlobalConst.VOLUME_OFFSET;
		int volume = soundKey.contains(GlobalConst.BGM_KEY) ? UserConfig.BGM_VOLUME : UserConfig.EFFECT_VOLUME;
		control.setValue(range * (GlobalConst.MAX_VOLUME - volume) / GlobalConst.MAX_VOLUME);
	}
}
