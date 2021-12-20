package best.asoul.aircraft.constant;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @Description 全局常量池
 * @Author Enchantedyou
 * @Date 2021年11月19日-23:34
 */
public class GlobalConst {

	private GlobalConst() {
	}

	/** 退出码：正常关闭 **/
	public static final int EXIT_SUCCESS_CODE = 0;
	/** 随机数种子对象 **/
	public static final Random RANDOM = new SecureRandom();
	/** 默认帧率 **/
	public static final int DEFAULT_FRAME_RATE = 60;
	/** 最大帧率 **/
	public static final int MAX_FRAME_RATE = 100;
	/** 最小帧率 **/
	public static final int MIN_FRAME_RATE = 30;
	/** 飞机碰撞伤害计算间隔 **/
	public static final long AIRCRAFT_COLLISION_INTERVAL = 1000L;
	/** 玩家飞机碰撞到普通飞机时扣除血量百分比 **/
	public static final int COLLISION_DEDUCT_NORMAL = 10;
	/** 玩家飞机碰撞到boss时扣除血量百分比 **/
	public static final int COLLISION_DEDUCT_BOSS = 30;
	/** 背景音乐循环次数 **/
	public static final int BGM_LOOP_COUNT = 999;
	/** 最大音量 **/
	public static final int MAX_VOLUME = 100;
	/** 音量计算偏移量 **/
	public static final int VOLUME_OFFSET = 50;
	/** 背景音乐关键字 **/
	public static final String BGM_KEY = "bgm";
	/** 增强效果增强Y轴偏移量 **/
	public static final int BOOST_OFFSET_Y = 30;
	/** 子弹等级1 **/
	public static final int BULLET_LEVEL_1 = 1;
	/** 子弹等级2 **/
	public static final int BULLET_LEVEL_2 = 2;
	/** 暴走等级（当子弹等级到达暴走等级时触发暴走） **/
	public static final int ENERGY_RESTORED_LEVEL = 3;
	/** 并排子弹间距 **/
	public static final int BULLET_DISTANCE = 10;
	/** 敌机阵亡后出现增益效果的概率 **/
	public static final int BOOST_CREATE_PROBABILITY = 45;
	/** 鼠标距离战机距离的上限，超过该距离则不随着鼠标移动，防止作弊 **/
	public static final int MOUSE_AIRCRAFT_DISTANCE_LIMIT = 800;
	/** 每个象限的最大角度 **/
	public static final double MAX_QUADRANT_DEGREES = 90D;
	/** 一圈对应的角度 **/
	public static final double DEGREES_OF_CIRCLE = MAX_QUADRANT_DEGREES * 4;
	/** 安全血线百分比 **/
	public static final double SAFE_BLOOD_PERCENT = 30D;
}
