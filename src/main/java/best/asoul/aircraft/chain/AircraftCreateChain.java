package best.asoul.aircraft.chain;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import best.asoul.aircraft.config.FlyingConfig;
import best.asoul.aircraft.constant.GlobalConst;
import best.asoul.aircraft.context.GameContext;
import best.asoul.aircraft.element.base.Aircraft;
import best.asoul.aircraft.entity.AircraftCamp;
import best.asoul.aircraft.handler.aircraft.AircraftCreateHandler;
import best.asoul.aircraft.util.AsoulUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 战机生成的链，控制关卡如何生成战机
 * @Author Enchantedyou
 * @Date 2021年11月22日-21:48
 */
@Slf4j
public class AircraftCreateChain {

	/** 战机生成处理器链表 **/
	private List<AircraftCreateHandler> aircraftCreateHandlerList = new LinkedList<>();

	public AircraftCreateChain(AircraftCreateHandler firstHandler) {
		aircraftCreateHandlerList.add(firstHandler);
	}

	public AircraftCreateChain() {
	}

	/**
	 * @Description 执行该责任链生成战机
	 * @Author Enchantedyou
	 * @Date 2021/11/22-22:07
	 */
	public void doChain() {
		for (AircraftCreateHandler currentHandler : aircraftCreateHandlerList) {
			// 执行创建任务
			final Aircraft mainAircraft = currentHandler.create();
			// 下一个任务执行前的处理
			currentHandler.beforeNextHandle();
			// 如果创建的主战机是boss，则播放boss爆炸动画
			dealBossExplode(mainAircraft);
		}
	}

	private void dealBossExplode(Aircraft mainAircraft) {
		if (mainAircraft.getCamp() != AircraftCamp.BOSS) {
			return;
		}
		final List<Aircraft> aircraftList = GameContext.getStageDefine().getToBeRemovedAircraftList();
		for (Aircraft enemy : aircraftList) {
			if (enemy.getImageKey().equals(mainAircraft.getImageKey())) {
				mainAircraft = enemy;
				break;
			}
		}

		final Aircraft explodeAircraft = AsoulUtil.clone(mainAircraft, Aircraft.class);
		final BufferedImage aircraftImage = explodeAircraft.getImage();
		final FlyingConfig mainAircraftConfig = mainAircraft.getConfig();
		for (int i = 0; i < GlobalConst.BOSS_DEAD_EXPLODE_COUNT; i++) {
			explodeAircraft.getConfig().determinePosition(
					GlobalConst.RANDOM.nextInt(aircraftImage.getWidth()) + mainAircraftConfig.getX(),
					GlobalConst.RANDOM.nextInt(aircraftImage.getHeight()) + mainAircraftConfig.getY());
			explodeAircraft.explodeAfterDead();
			AsoulUtil.pause(GlobalConst.BOSS_DEAD_EXPLODE_INTERVAL);
		}
		AsoulUtil.pause(1000L);
	}

	/**
	 * @Description 追加战机生成处理器
	 * @Author Enchantedyou
	 * @Date 2021/12/5-17:25
	 * @param handler
	 */
	public AircraftCreateChain append(AircraftCreateHandler handler) {
		if (null != handler) {
			this.aircraftCreateHandlerList.add(handler);
		}
		return this;
	}
}
