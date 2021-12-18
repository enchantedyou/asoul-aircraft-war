package best.asoul.aircraft.component;

import java.awt.*;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description Jframe组件类
 * @Author Enchantedyou
 * @Date 2021年11月20日-14:48
 */
@Slf4j
public class AsoulJFrame extends JFrame {

	public AsoulJFrame() {
		this.setTitle("枝江飞机大战");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);

		final GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		graphicsDevice.setFullScreenWindow(this);
	}
}
