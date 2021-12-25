package best.asoul.aircraft.config;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.factory.ResourceLoader;

/**
 * @Description 用户配置
 * @Author Enchantedyou
 * @Date 2021年11月22日-20:20
 */
public class UserConfig {

	private static final AtomicReference<UserConfig> INSTANCE = new AtomicReference<>();

	private UserConfig() {
	}

	/**
	 * @Description 获取用户配置实例
	 * @Author Enchantedyou
	 * @Date 2021/12/23-14:36
	 * @return best.asoul.aircraft.config.UserConfig
	 */
	public static UserConfig getInstance() {
		if (null == INSTANCE.get()) {
			synchronized (UserConfig.class) {
				if (null == INSTANCE.get()) {
					// 加载配置文件
					final Properties properties = ResourceLoader.loadUserConfig();
					UserConfig userConfig = new UserConfig();
					// 读取配置
					final Object frameRate = properties.get(GlobalConfig.USER_CONFIG_PROP_PREFIX + "frameRate");
					final Object bgmVolume = properties.get(GlobalConfig.USER_CONFIG_PROP_PREFIX + "bgmVolume");
					final Object effectVolume = properties.get(GlobalConfig.USER_CONFIG_PROP_PREFIX + "effectVolume");

					// 赋值
					if (frameRate != null) {
						userConfig.setFrameRate(Integer.parseInt(String.valueOf(frameRate)));
					}
					if (bgmVolume != null) {
						userConfig.setBgmVolume(Integer.parseInt(String.valueOf(bgmVolume)));
					}
					if (effectVolume != null) {
						userConfig.setEffectVolume(Integer.parseInt(String.valueOf(effectVolume)));
					}
					INSTANCE.getAndSet(userConfig);
				}
			}
		}
		return INSTANCE.get();
	}

	/** 帧率 **/
	private int frameRate = 90;
	/** 背景音乐音量 **/
	private int bgmVolume = 75;
	/** 音效音量 **/
	private int effectVolume = 75;

	public int getFrameRate() {
		return frameRate;
	}

	public int getBgmVolume() {
		return bgmVolume;
	}

	public int getEffectVolume() {
		return effectVolume;
	}

	public void setFrameRate(Integer frameRate) {
		if (null == frameRate || frameRate > GlobalConst.MAX_FRAME_RATE || frameRate < GlobalConst.MIN_FRAME_RATE) {
			return;
		}
		this.frameRate = frameRate;
	}

	public void setBgmVolume(Integer bgmVolume) {
		if (null == bgmVolume || bgmVolume > GlobalConst.MAX_VOLUME || bgmVolume < 0) {
			return;
		}
		this.bgmVolume = bgmVolume;
	}

	public void setEffectVolume(Integer effectVolume) {
		if (null == effectVolume || effectVolume > GlobalConst.MAX_VOLUME || effectVolume < 0) {
			return;
		}
		this.effectVolume = effectVolume;
	}

	@Override
	public String toString() {
		return "UserConfig{" +
				"frameRate=" + frameRate +
				", bgmVolume=" + bgmVolume +
				", effectVolume=" + effectVolume +
				'}';
	}
}
