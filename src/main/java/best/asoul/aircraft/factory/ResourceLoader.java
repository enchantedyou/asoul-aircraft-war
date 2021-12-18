package best.asoul.aircraft.factory;

import java.io.File;

import best.asoul.aircraft.invoker.FileLoadInvoker;

/**
 * @Description 资源加载器
 * @Author Enchantedyou
 * @Date 2021年11月20日-01:12
 */
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
		loadFile(ResourceLoader.class.getResource("/image").getPath(), ImageResourceFactory::fillImageResource);
	}

	/**
	 * @Description 加载音效资源
	 * @Author Enchantedyou
	 * @Date 2021/11/26-21:05
	 */
	public static void loadSound() {
		loadFile(ResourceLoader.class.getResource("/sound").getPath(), SoundResourceFactory::fillAudioResource);
	}

	/**
	 * @Description 加载动画资源
	 * @Author Enchantedyou
	 * @Date 2021/11/27-17:58
	 */
	public static void loadAnimation() {
		loadFile(ResourceLoader.class.getResource("/animation").getPath(),
				AnimationResourceFactory::fillAnimationResource);
	}
}