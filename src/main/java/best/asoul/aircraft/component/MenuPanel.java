package best.asoul.aircraft.component;

import java.awt.*;

import javax.swing.*;

import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.factory.ImageResourceFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 菜单面板
 * @Author Enchantedyou
 * @Date 2021/11/27-13:13
 */
@Slf4j
public class MenuPanel extends JPanel {

	public MenuPanel() {
		this.setDoubleBuffered(true);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(ImageResourceFactory.getImage(ResourceConst.MENU_BACKGROUND), 0, 0, getSize().width,
				getSize().height, this);
	}
}
