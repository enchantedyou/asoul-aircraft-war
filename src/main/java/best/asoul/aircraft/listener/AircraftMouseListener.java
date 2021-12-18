package best.asoul.aircraft.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.GameStatus;

/**
 * @Description 战机按键监听
 * @Author Enchantedyou
 * @Date 2021年11月20日-16:34
 */
public class AircraftMouseListener implements MouseMotionListener, MouseListener {

	/** 被监听的战机 **/
	private Aircraft aircraft;

	public AircraftMouseListener(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		aircraftMove(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		aircraftMove(e);
	}

	private void aircraftMove(MouseEvent e) {
		if (GameContext.getGameSession().getGameStatus() != GameStatus.RUNNING || aircraft.isDead()) {
			return;
		}
		final BufferedImage image = aircraft.getImage();
		final FlyingConfig flyingConfig = aircraft.getConfig();
		// 鼠标和战机的距离过远则战机不再随鼠标移动，防止作弊
		boolean isMove = Math.abs(flyingConfig.getX() - e.getX()) <= GlobalConst.MOUSE_AIRCRAFT_DISTANCE_LIMIT
				&& Math.abs(flyingConfig.getY() - e.getY()) <= GlobalConst.MOUSE_AIRCRAFT_DISTANCE_LIMIT;
		if (isMove) {
			// 让鼠标在战机的中心位置
			aircraft.moveTo(e.getX() - (image.getWidth() / 2), e.getY() - (image.getHeight() / 2));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
