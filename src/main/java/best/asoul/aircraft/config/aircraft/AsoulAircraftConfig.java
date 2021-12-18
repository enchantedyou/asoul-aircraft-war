package best.asoul.aircraft.config.aircraft;

import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.entity.Direction;

/**
 * @Description 战机基本配置
 * @Author Enchantedyou
 * @Date 2021年11月20日-22:07
 */
public class AsoulAircraftConfig extends FlyingConfig implements Serializable {

	public AsoulAircraftConfig() {
		super.width = 20;
		super.height = 20;
		super.x = (GlobalConfig.SCREEN_WIDTH - width) / 2;
		super.y = (int) (GlobalConfig.SCREEN_HEIGHT * 0.8);
		super.createInterval = 2000;
		super.moveInterval = 20;
		super.degrees = Direction.UP.degrees();
	}
}
