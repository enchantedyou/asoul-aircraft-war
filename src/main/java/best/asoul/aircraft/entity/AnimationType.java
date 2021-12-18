package best.asoul.aircraft.entity;

/**
 * @Description 动画效果类型
 * @Author Enchantedyou
 * @Date 2021年11月27日-17:47
 */
public enum AnimationType {
	/** 飞机爆炸动画 **/
	AIRCRAFT_EXPLODE("aircraft_explode", 8, 300, 300, 0, 3, false),
	/** 子弹升级buff动画 **/
	BULLET_LEVEL_UP("bullet_level_up", 14, 109, 67, 0, 5, true),
	/** buff光环动画 **/
	BOOST_HALO("boost_halo", 8, 92, 92, 0, 6, true),
	/** 治疗动画 **/
	AIRCRAFT_TREATMENT("aircraft_treatment", 3, 114, 136, 0, 10, true),
	/** 护盾buff动画 **/
	AIRCRAFT_SHIELD("aircraft_shield", 20, 82, 60, 0, 5, true),
	/** 护盾生效动画 **/
	SHIELD_PROTECT("shield_protect", 1, 156, 156, 0, 5, true),
	/** 向晚觉醒buff动画 **/
	AVA_AWAKE("ava_awake", 1, 67, 67, 0, 1, true);

	/** 动画的key **/
	private String key;
	/** 总帧数 **/
	private int frameCount;
	/** 每一帧的宽 **/
	private int width;
	/** 每一帧的高 **/
	private int height;
	/** 从哪个Y轴起始点开始裁剪 **/
	private int cutY;
	/** 每帧播放多少次 **/
	private int playTimesPerFrame;
	/** 是否循环 **/
	private boolean loop;

	AnimationType(String key, int frameCount, int width, int height, int cutY, int playTimesPerFrame, boolean loop) {
		this.key = key;
		this.frameCount = frameCount;
		this.width = width;
		this.height = height;
		this.cutY = cutY;
		this.playTimesPerFrame = playTimesPerFrame;
		this.loop = loop;
	}

	public String getKey() {
		return key;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCutY() {
		return cutY;
	}

	public int getPlayTimesPerFrame() {
		return playTimesPerFrame;
	}

	public boolean isLoop() {
		return loop;
	}
}
