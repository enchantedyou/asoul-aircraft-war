package best.asoul.aircraft.factory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import best.asoul.aircraft.element.base.Flying;
import best.asoul.aircraft.entity.AnimationEffectPlayer;
import best.asoul.aircraft.entity.AnimationType;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.handler.resource.ResourceDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 动画效果工厂
 * @Author Enchantedyou
 * @Date 2021/11/27-17:36
 */
@Slf4j
public class AnimationResourceFactory {

	private AnimationResourceFactory() {
	}

	/** 动画资源 **/
	private static final Map<String, List<BufferedImage>> ANIMATION_MAP = new ConcurrentHashMap<>();

	/**
	 * @Description 构建动画播放器
	 * @Author Enchantedyou
	 * @Date 2021/11/27-18:03
	 * @param animationType
	 * @param flying
	 * @return best.asoul.aircraft.entity.AnimationEffectPlayer
	 */
	public static AnimationEffectPlayer buildAnimationPlayer(AnimationType animationType, Flying flying) {
		final AnimationEffectPlayer effectPlayer = new AnimationEffectPlayer(animationType, flying);
		final List<BufferedImage> imageList = ANIMATION_MAP.get(animationType.getKey());
		effectPlayer.setImageList(imageList);

		if (null == imageList || imageList.isEmpty()) {
			throw new AsoulException("动画帧列表不存在：" + animationType.getKey());
		}
		return effectPlayer;
	}

	/**
	 * @Description 填充动画资源
	 * @Author Enchantedyou
	 * @Date 2021/11/27-17:57
	 * @param file
	 */
	protected static void fillAnimationResource(File file) {
		ResourceDecoder.decode(file, (fileName, inputStream) -> {
			final String animationKey = fileName.split("\\.")[0];
			log.info("加载动画资源：{}", animationKey);
			try {
				final BufferedImage image = ImageIO.read(inputStream);
				final AnimationType effectType = AnimationType.valueOf(animationKey.toUpperCase());
				List<BufferedImage> imageList = new CopyOnWriteArrayList<>();

				for (int i = 0; i < effectType.getFrameCount(); i++) {
					final BufferedImage subImage = image.getSubimage(effectType.getWidth() * i, effectType.getCutY(),
							effectType.getWidth(), effectType.getHeight());
					imageList.add(subImage);
				}
				ANIMATION_MAP.put(animationKey, imageList);
			} catch (Exception e) {
				log.error("动画资源加载失败：", e);
			}
		});
	}
}
