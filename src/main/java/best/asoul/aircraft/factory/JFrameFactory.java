package best.asoul.aircraft.factory;

import javax.swing.*;

import best.asoul.aircraft.component.AsoulJFrame;
import best.asoul.aircraft.component.MenuPanel;
import best.asoul.aircraft.component.stage.StagePanel;
import best.asoul.aircraft.invoker.Invoker;
import best.asoul.aircraft.listener.GameProcessControlListener;

/**
 * @Description jframe工厂
 * @Author Enchantedyou
 * @Date 2021年11月27日-13:45
 */
public class JFrameFactory {

	private JFrameFactory() {
	}

	/**
	 * @Description 创建菜单界面
	 * @Author Enchantedyou
	 * @Date 2021/11/27-13:53
	 * @param afterCreateInvoker
	 */
	public static void createMenuJFrame(Invoker afterCreateInvoker) {
		createJFrame(new MenuPanel(), afterCreateInvoker);
	}

	/**
	 * @Description 创建关卡1界面
	 * @Author Enchantedyou
	 * @Date 2021/11/27-13:54
	 * @param afterCreateInvoker
	 */
	public static void createStage1JFrame(Invoker afterCreateInvoker) {
		createJFrame(new StagePanel(), afterCreateInvoker);
	}

	private static void createJFrame(JPanel jPanel, Invoker afterCreateInvoker) {
		AsoulJFrame asoulJFrame = new AsoulJFrame();
		asoulJFrame.addKeyListener(new GameProcessControlListener(asoulJFrame));
		asoulJFrame.add(jPanel);
		asoulJFrame.setVisible(true);

		if (afterCreateInvoker != null) {
			afterCreateInvoker.invoke();
		}
	}
}
