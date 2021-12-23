package best.asoul.aircraft.factory;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import best.asoul.aircraft.config.UserConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.handler.resource.ResourceDecoder;
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
	private static final Map<String, InputStream> AUDIO_MAP = new ConcurrentHashMap<>();

	/**
	 * @Description 获取音效资源文件
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:00
	 * @param soundKey
	 * @return java.net.URL
	 */
	public static Clip getAudioClip(String soundKey) {
		try {
			Clip audioClip = AudioSystem.getClip();
			audioClip.open(AudioSystem.getAudioInputStream(AUDIO_MAP.get(soundKey)));
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
		ResourceDecoder.decode(file, (fileName, inputStream) -> {
			final String soundKey = fileName.split("\\.")[0];
			log.info("加载音效资源：{}", soundKey);
			AUDIO_MAP.put(soundKey, inputStream);
		});
	}

	private static void controlVolume(String soundKey, Clip audioClip) {
		final UserConfig userConfig = UserConfig.getInstance();
		FloatControl control = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
		final float range = control.getMinimum() + GlobalConst.VOLUME_OFFSET;
		int volume = soundKey.contains(GlobalConst.BGM_KEY) ? userConfig.getBgmVolume() : userConfig.getEffectVolume();
		control.setValue(range * (GlobalConst.MAX_VOLUME - volume) / GlobalConst.MAX_VOLUME);
	}
}
