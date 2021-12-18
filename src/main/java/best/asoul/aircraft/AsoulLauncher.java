package best.asoul.aircraft;

import best.asoul.aircraft.factory.JFrameFactory;
import best.asoul.aircraft.factory.ResourceLoader;
import best.asoul.aircraft.util.SoundUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 启动器，游戏入口
 * @Author Enchantedyou
 * @Date 2021/11/19-23:19
 */
@Slf4j
public class AsoulLauncher {

	public static void main(String[] args) {
		// 加载资源
		loadResource();
		// 启动主界面
		JFrameFactory.createMenuJFrame(SoundUtil::loopMenuBgm);
	}

	/**
	 * @Description 加载资源
	 * @Author Enchantedyou
	 * @Date 2021/11/20-14:38
	 */
	private static void loadResource() {
		ResourceLoader.loadImage();
		log.info(">>>图片资源加载完成<<<");
		ResourceLoader.loadSound();
		log.info(">>>音效资源加载完成<<<");
		ResourceLoader.loadAnimation();
		log.info(">>>动画资源加载完成<<<");
	}
}
