package priv.lt.snake.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 工具类
 * 
 * 此类中存放了其他类中用到的一些常量
 * 并且支持配置文件
 * 配置文件的路径为游戏运行的目录, 文件名为 snake.ini
 * 
 * 配置文件的写法请参见字段的注释
 * 
 * 配置文件中设置项可以只写需要配置的, 没有写的设置项默认为缺省值
 * 各配置项的缺省值请参见字段的注释
 * 
 * 每个配置项都有设置值范围, 超出范围(无效)的设置值将不起作用
 * 设置值的范围请参见字段的注释 
 */
public class GlobalSet {

	private static Properties properties = new Properties();
	//配置文件的路径(默认为当前目录下的 snake.ini文件)
	private static String CONFIG_FILE = "snake.ini";

	public static final int CELL_WIDTH;
	public static final int CELL_HEIGHT;
	public static final int WIDTH;
	public static final int HEIGHT;
	// 显示的像素宽度 (格子宽度度 * 每一格的宽度)
	public static final int CANVAS_WIDTH;
	// 显示的像素高度 (格子高度 * 每一格的高度)
	public static final int CANVAS_HEIGHT;
	//蛇的初始长度, 对应的配置文件中的关键字为: init_length<BR>
	public static final int INIT_LENGTH;
	//蛇的初始速度 (单位: 毫秒/格)<BR>
	public static final int SPEED;
	//蛇每次加速或减速的幅度 (单位: 毫秒/格)<BR>
	public static final int SPEED_STEP;
	
	public static final String TITLE_LABEL_TEXT;
	public static final String INFO_LABEL_TEXT;

	private GlobalSet() {}

	//初始化常量,try异常处理,catch捕获异常
	static {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(CONFIG_FILE);
			properties.load(inputStream);
		} catch (Exception e) {
			System.out.println("没有配置文件");
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Integer temp = null;
		/* 没有设置或设置的无效时要有一个默认值 */
		WIDTH = (temp = getIntValue("width")) != null && temp <= 80
				&& temp >= 10 ? temp : 35;
		HEIGHT = (temp = getIntValue("height")) != null && temp <= 60
				&& temp >= 10 ? temp : 20;
		INIT_LENGTH = (temp = getIntValue("init_length")) != null && temp > 1
				&& temp < WIDTH ? temp : 2;
		SPEED = (temp = getIntValue("speed")) != null && temp >= 10 ? temp
				: 200;
		SPEED_STEP = (temp = getIntValue("speed_step")) != null && temp >= 1 ? temp
				: 25;

		int defaultCellSize = (temp = getIntValue("cell_size")) != null
				&& temp > 0 && temp <= 100 ? temp : 20;
		CELL_WIDTH = (temp = getIntValue("cell_width")) != null && temp > 0
				&& temp <= 100 ? temp : defaultCellSize;
		CELL_HEIGHT = (temp = getIntValue("cell_height")) != null && temp > 0
				&& temp <= 100 ? temp : defaultCellSize;

		CANVAS_WIDTH = WIDTH * CELL_WIDTH;
		CANVAS_HEIGHT = HEIGHT * CELL_HEIGHT;

		String tempStr = null;
		TITLE_LABEL_TEXT = (tempStr = getValue("title")) == null ? "说明："
				: tempStr;
		INFO_LABEL_TEXT = (tempStr = getValue("info")) == null ? "方向键控制方向, 回车键暂停/继续\n\nPAGE UP, PAGE DOWN 加速或减速 "
				: tempStr;
	}

	/**
	 * 要考虑多种情况<BR>
	 * 1. 没有这个key和value<BR>
	 * 2 有key 没有 value
	 */
	private static Integer getIntValue(String key) {
		if (key == null)
			throw new RuntimeException("key 不能为空");
		try {
			return new Integer(properties.getProperty(key));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static String getValue(String key) {
		try {
			return new String(properties.getProperty(key).getBytes("iso8859-1"));
		} catch (Exception e) {
			return null;
		}
	}
}
