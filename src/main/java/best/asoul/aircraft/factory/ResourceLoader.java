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
	 * @Description 递归加载文件
	 * @Author Enchantedyou
	 * @Date 2021/11/20-14:32
	 * @param rootPath
	 * @param callBack
	 */
	private static void loadFile(String rootPath, FileLoadInvoker callBack) {
		final File[] files = new File(rootPath).listFiles();
		if (null == files || files.length == 0) {
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				loadFile(file.getPath(), callBack);
			} else {
				callBack.invoke(file);
			}
		}
	}

	/**
	 * @Description 加载图片资源
	 * @Author Enchantedyou
	 * @Date 2021/11/20-13:33
	 */
	public static void loadImage() {
		loadFile(getRootPath("/image"), ImageResourceFactory::fillImageResource);
	}

	/**
	 * @Description 加载音效资源
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:05
	 */
	public static void loadSound() {
		loadFile(getRootPath("/sound"), SoundResourceFactory::fillAudioResource);
	}

	/**
	 * @Description 加载动画资源
	 * @Author Enchantedyou
	 * @Date 2021/11/27-17:58
	 */
	public static void loadAnimation() {
		loadFile(getRootPath("/animation"), AnimationResourceFactory::fillAnimationResource);
	}

	/**
	 * @Description	加载用户配置
	 * @Author Enchantedyou
	 * @Date 2021/12/23-14:25
	 * @return java.util.Properties
	 */
	public static Properties loadUserConfig(){
		final String userConfigPath = getRootPath("/config") + File.separator + GlobalConfig.USER_CONFIG_FILE_NAME;
		try(final InputStream inputStream = Files.newInputStream(Paths.get(userConfigPath))){
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		}catch (Exception e){
			throw new AsoulException("用户配置加载失败", e);
		}
	}

	private static String getRootPath(String relativePath) {
		final URL url = ResourceLoader.class.getResource("/");
		if (url != null) {
			return url.getPath() + relativePath;
		}
		return System.getProperty("user.dir") + "/bin" + relativePath;
	}
}