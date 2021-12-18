package best.asoul.aircraft.config.boost;

import java.io.Serializable;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.constant.GlobalConst;

/**
 * @Description 敌机基本配置
 * @Author Enchantedyou
 * @Date 2021/11/21-18:35
 */
public class BoostConfig extends FlyingConfig implements Serializable {

	public BoostConfig(int width, int height) {
		super.width = width;
		super.height = height;
		super.x = GlobalConst.RANDOM.nextInt(GlobalConfig.SCREEN_WIDTH - width);
		super.y = GlobalConst.RANDOM.nextInt(GlobalConfig.SCREEN_HEIGHT - height);
		super.createInterval = -1;
		super.moveInterval = 20;
		super.speed = 3;
	}

	@Override
	public int getCreateInterval() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCreateInterval(int createInterval) {
		throw new UnsupportedOperationException();
	}
}
