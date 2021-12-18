package best.asoul.aircraft.config.aircraft;

import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.entity.Direction;

/**
 * @Description 敌机基本配置
 * @Author Enchantedyou
 * @Date 2021/11/21-18:35
 */
public class EnemyAircraftConfig extends FlyingConfig implements Serializable {

	public EnemyAircraftConfig() {
		super.width = 98;
		super.height = 75;
		super.x = (GlobalConfig.SCREEN_WIDTH - width) / 2;
		super.y = (int) (GlobalConfig.SCREEN_HEIGHT * 0.8);
		super.createInterval = 20;
		super.moveInterval = 10;
		super.speed = 5;
		super.degrees = Direction.DOWN.degrees();
	}
}
