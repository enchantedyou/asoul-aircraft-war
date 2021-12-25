package best.asoul.aircraft.config.bullet;

import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.entity.Direction;

/**
 * @Description 一个魂阵营战机普通子弹基础配置
 * @Author Enchantedyou
 * @Date 2021/11/20-22:13
 */
public class AsoulBulletConfig extends FlyingConfig implements Serializable {

	public AsoulBulletConfig(int width, int height) {
		super.width = width;
		super.height = height;
		super.x = 0;
		super.y = 0;
		// 偏移量100
		super.createInterval = 145;
		// 偏移量4
		super.moveInterval = 10;
		super.speed = 11;
		super.degrees = Direction.UP.degrees();
	}
}
