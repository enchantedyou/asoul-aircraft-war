package best.asoul.aircraft.config.bullet;

import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.entity.Direction;

/**
 * @Description 敌方阵营普通子弹基础配置
 * @Author Enchantedyou
 * @Date 2021/11/21-17:24
 */
public class EnemyBulletConfig extends FlyingConfig implements Serializable {

	public EnemyBulletConfig(int width, int height) {
		super.width = width;
		super.height = height;
		super.x = 0;
		super.y = 0;
		super.createInterval = 500;
		super.moveInterval = 25;
		super.speed = 16;
		super.degrees = Direction.DOWN.degrees();
	}
}
