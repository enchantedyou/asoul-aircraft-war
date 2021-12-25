package best.asoul.aircraft.util;

import java.awt.*;
import java.awt.image.BufferedImage;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.constant.ResourceConst;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.element.base.Bullet;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.entity.Quadrant;
import best.asoul.aircraft.factory.ImageResourceFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 画笔工具类
 * @Author Enchantedyou
 * @Date 2021年11月20日-15:21
 */
@Slf4j
public class JFrameUtil {

	private JFrameUtil() {
	}

	/**
	 * @Description 绘制战机
	 * @Author Enchantedyou
	 * @Date 2021/12/8-22:03
	 * @param g
	 * @param aircraft
	 */
	public static void drawAircraft(Graphics g, Aircraft aircraft) {
		if (aircraft.isDead()) {
			return;
		}
		final BufferedImage image = aircraft.getImage();
		final FlyingConfig aircraftConfig = aircraft.getConfig();
		g.drawImage(image, aircraftConfig.getX(), aircraftConfig.getY(), null);
		if (aircraft.getCamp() == AircraftCamp.ASOUL) {
			// 画尾焰
			final BufferedImage tailFlameImage = ImageResourceFactory.getImage(ResourceConst.TAIL_FLAME);
			g.drawImage(tailFlameImage, aircraftConfig.getX() + 18, aircraftConfig.getY() + image.getHeight() - 30,
					null);
			g.drawImage(tailFlameImage, aircraftConfig.getX() + 35, aircraftConfig.getY() + image.getHeight() - 30,
					null);
		}
	}

	/**
	 * @Description 绘制子弹
	 * @Author Enchantedyou
	 * @Date 2021/12/8-22:05
	 * @param g
	 * @param bullet
	 */
	public static void drawBullet(Graphics2D g, Bullet bullet, Aircraft aircraft) {
		final FlyingConfig bulletConfig = bullet.getConfig();
		double degrees = getFixedDegrees(bulletConfig.getDegrees());
		// 纠正子弹角度，在子弹贴图方向是下的情况下，纠正方向，使其以离开飞机的方向运动
		final Quadrant quadrant = getQuadrant(bulletConfig.getDegrees());
		if (quadrant == Quadrant.NONE) {
			degrees = -degrees - GlobalConst.MAX_QUADRANT_DEGREES;
		} else {
			// 垂直方向的子弹方向纠正：0°时-90°，90°时-180°，180°时-270°，270°时-360°
			final double multiple = bulletConfig.getDegrees() / GlobalConst.MAX_QUADRANT_DEGREES;
			degrees = -(multiple + 1D) * GlobalConst.MAX_QUADRANT_DEGREES;
		}
		final Composite originalComposite = g.getComposite();
		if (aircraft.getCamp() == AircraftCamp.ASOUL) {
			// 玩家的子弹朝上，所以反转
			degrees += GlobalConst.MAX_QUADRANT_DEGREES * 2;
			// 玩家子弹透明度（暴走时变亮一点）
			float alpha = aircraft.getBulletLevel() < GlobalConst.ENERGY_RESTORED_LEVEL ? 0.8f : 0.9f;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		}
		BufferedImage image = rotateImage(bullet.getImage(), degrees);
		g.drawImage(image, bulletConfig.getX(), bulletConfig.getY(), null);
		g.setComposite(originalComposite);
	}

	/**
	 * @Description 获取纠正后的角度
	 * @Author Enchantedyou
	 * @Date 2021/12/8-22:01
	 * @param degrees
	 * @return double
	 */
	public static double getFixedDegrees(double degrees) {
		while (degrees < 0D && degrees <= GlobalConst.DEGREES_OF_CIRCLE) {
			degrees += GlobalConst.DEGREES_OF_CIRCLE;
		}
		return degrees % GlobalConst.DEGREES_OF_CIRCLE;
	}

	/**
	 * @param bufferedimage
	 * @param degree
	 * @return java.awt.image.BufferedImage
	 * @Description 图片旋转
	 * @Author Enchantedyou
	 * @Date 2021/12/5-22:38
	 */
	public static BufferedImage rotateImage(final BufferedImage bufferedimage,
			final double degree) {
		int w = bufferedimage.getWidth();
		int h = bufferedimage.getHeight();
		int type = bufferedimage.getColorModel().getTransparency();

		BufferedImage img = new BufferedImage(w, h, type);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.rotate(Math.toRadians(degree), w / 2D, h / 2D);
		g.drawImage(bufferedimage, 0, 0, null);
		g.dispose();
		return img;
	}

	/**
	 * @param g
	 * @param imageKey
	 * @param yOffset
	 * @Description 将图片画到x轴的正中心
	 * @Author Enchantedyou
	 * @Date 2021/12/5-13:43
	 */
	public static void drawImageXCenter(Graphics g, String imageKey, int yOffset) {
		final BufferedImage image = ImageResourceFactory.getImage(imageKey);
		int x = (GlobalConfig.SCREEN_WIDTH - image.getWidth()) / 2;
		g.drawImage(image, x, yOffset, null);
	}

	/**
	 * @param g
	 * @param xOffset
	 * @param yOffset
	 * @param aircraft
	 * @Description 绘制飞机的血量
	 * @Author Enchantedyou
	 * @Date 2021/12/5-22:52
	 */
	public static void drawBlood(Graphics2D g, int xOffset, int yOffset, Aircraft aircraft) {
		BufferedImage image;
		int x = xOffset;
		int y = yOffset;
		final double widthPercent = aircraft.getHealthPointPercent();
		final FlyingConfig aircraftConfig = aircraft.getConfig();

		if (aircraft.getCamp() == AircraftCamp.ASOUL) {
			if (widthPercent > GlobalConst.SAFE_BLOOD_PERCENT) {
				image = ImageResourceFactory.getImage(ResourceConst.PLAYER_SAFE_BLOOD_LINE);
			} else {
				image = ImageResourceFactory.getImage(ResourceConst.PLAYER_DANGER_BLOOD_LINE);
			}
			x += (GlobalConfig.SCREEN_WIDTH - image.getWidth()) / 2;
		} else {
			image = ImageResourceFactory.getImage(ResourceConst.ENEMY_BLOOD_LINE);
			x += aircraftConfig.getX();
			y += aircraftConfig.getY();
		}

		if (widthPercent > 0) {
			if (aircraft.getCamp() == AircraftCamp.ASOUL) {
				final BufferedImage subImage = image.getSubimage(0, 0, (int) (image.getWidth() * widthPercent / 100),
						image.getHeight());
				g.drawImage(subImage, x, y, null);
			} else if (y > -aircraft.getImage().getHeight()) {
				final Composite originalComposite = g.getComposite();
				// 用透明画笔绘制
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g.drawImage(image, x, y, (int) (aircraft.getImage().getWidth() * widthPercent / 100), image.getHeight(),
						null);
				// 还原画笔透明度
				g.setComposite(originalComposite);
			}
		} else {
			g.drawImage(image, x, y, 0, 0, null);
		}
	}

	/**
	 * @param degrees
	 * @return best.asoul.aircraft.entity.Quadrant
	 * @Description 获取当前角度的象限
	 * @Author Enchantedyou
	 * @Date 2021/12/5-22:54
	 */
	public static Quadrant getQuadrant(double degrees) {
		degrees = getFixedDegrees(degrees);
		if (degrees > Quadrant.ONE.degreesOffset() && degrees < Quadrant.TWO.degreesOffset()) {
			return Quadrant.ONE;
		} else if (degrees > Quadrant.TWO.degreesOffset() && degrees < Quadrant.THREE.degreesOffset()) {
			return Quadrant.TWO;
		} else if (degrees > Quadrant.THREE.degreesOffset() && degrees < Quadrant.FOUR.degreesOffset()) {
			return Quadrant.THREE;
		} else if (degrees > Quadrant.FOUR.degreesOffset()) {
			return Quadrant.FOUR;
		}
		return Quadrant.NONE;
	}
}
