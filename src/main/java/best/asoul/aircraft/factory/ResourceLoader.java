package best.asoul.aircraft.factory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.invoker.FileLoadInvoker;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 资源加载器
 * @Author Enchantedyou
 * @Date 2021年11月20日-01:12
 */
@Slf4j
public class ResourceLoader {

	private ResourceLoader() {
	}

	/**
	 * @Description 加载资源文件
	 * @Author Enchantedyou
	 * @Date 2021/11/20-14:32
	 * @param resourceFilePath
	 * @param callBack
	 */
	private static void loadFile(String resourceFilePath, FileLoadInvoker callBack) {
		final File resourceFile = Paths.get(resourceFilePath).toFile();
		if (!resourceFile.exists() || resourceFile.isDirectory()) {
			log.warn("资源文件丢失：{}", resourceFile.getPath());
			return;
		}
		callBack.invoke(resourceFile);
	}

	/**
	 * @Description 加载图片资源
	 * @Author Enchantedyou
	 * @Date 2021/11/20-13:33
	 */
	public static void loadImage() {
		loadFile(getFullPath("/image" + GlobalConfig.SECRET_FILE_SUFFIX), ImageResourceFactory::fillImageResource);
	}

	/**
	 * @Description 加载音效资源
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:05
	 */
	public static void loadSound() {
		loadFile(getFullPath("/sound" + GlobalConfig.SECRET_FILE_SUFFIX), SoundResourceFactory::fillAudioResource);
	}

	/**
	 * @Description 加载动画资源
	 * @Author Enchantedyou
	 * @Date 2021/11/27-17:58
	 */
	public static void loadAnimation() {
		loadFile(getFullPath("/animation" + GlobalConfig.SECRET_FILE_SUFFIX),
				AnimationResourceFactory::fillAnimationResource);
	}

	/**
	 * @Description 加载用户配置
	 * @Author Enchantedyou
	 * @Date 2021/12/23-14:25
	 * @return java.util.Properties
	 */
	public static Properties loadUserConfig() {
		final String userConfigPath = getFullPath("/config/" + GlobalConfig.USER_CONFIG_FILE_NAME);
		try (final InputStream inputStream = Files.newInputStream(Paths.get(userConfigPath))) {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (Exception e) {
			throw new AsoulException("用户配置加载失败", e);
		}
	}

	/**
	 * @Description 获取工程路径下文件的全路径
	 * @Author Enchantedyou
	 * @Date 2021/12/25-14:42
	 * @param relativePath
	 * @return java.lang.String
	 */
	public static String getFullPath(String relativePath) {
		final URL url = ResourceLoader.class.getResource("/");
		if (url != null) {
			return url.getPath().substring(1) + relativePath;
		}
		return System.getProperty("user.dir") + File.separator + "bin" + relativePath;
	}
}