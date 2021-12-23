package best.asoul.aircraft.factory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import best.asoul.aircraft.handler.resource.ResourceDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 图片资源工厂
 * @Author Enchantedyou
 * @Date 2021年11月20日-13:12
 */
@Slf4j
public class ImageResourceFactory {

	private ImageResourceFactory() {
	}

	/** 图片资源 **/
	private static final Map<String, BufferedImage> IMAGE_MAP = new ConcurrentHashMap<>();

	/**
	 * @Description 获取图片资源
	 * @Author Enchantedyou
	 * @Date 2021/11/20-13:19
	 * @param imageKey
	 * @return java.awt.image.BufferedImage
	 */
	public static BufferedImage getImage(String imageKey) {
		final BufferedImage image = IMAGE_MAP.get(imageKey);
		if (null == image) {
			log.warn("图片资源文件丢失：{}", imageKey);
		}
		return image;
	}

	/**
	 * @Description 填装图片资源
	 * @Author Enchantedyou
	 * @Date 2021/11/20-13:24
	 * @param file
	 */
	protected static void fillImageResource(File file) {
		ResourceDecoder.decode(file, (fileName, inputStream) -> {
			final String imageKey = fileName.split("\\.")[0];
			log.info("加载图片资源：{}", imageKey);
			try {
				IMAGE_MAP.put(imageKey, ImageIO.read(inputStream));
			} catch (IOException e) {
				log.error("图片资源加载失败", e);
			}
		});
	}
}
