package priv.lt.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import priv.lt.snake.util.GlobalSet;

// 地面
// 可以通过setRockColor(), setGriddingColor()方法更改石头或网格的颜色
// 通过setDrawGridding() 方法设置是否画网格
// 可以覆盖 drawRock方法以改变石头的显示方式
//  可以通过覆盖genernateRocks() 方法改变石头产生的布局
public class Ground {

	//存放石头的二维数组 
	private boolean rocks[][] = new boolean[GlobalSet.WIDTH][GlobalSet.HEIGHT];
	//存放getFreePoint()方法生成的不是石头的随机的坐标 
	private Point freePoint = new Point();
	public static final Color DEFAULT_ROCK_COLOR = new Color(0x666666);//默认灰色石头
	private Color rockColor = DEFAULT_ROCK_COLOR;
	public static final Color DEFAULT_GRIDDING_COLOR = Color.LIGHT_GRAY;//默认亮灰色网格
	private Color griddingColor = DEFAULT_GRIDDING_COLOR;	
	private Random random = new Random();
	private boolean drawGridding = false;//网格开关，默认关

    //清空石头
	public void clear() {
		for (int x = 0; x < GlobalSet.WIDTH; x++)
			for (int y = 0; y < GlobalSet.HEIGHT; y++)
				rocks[x][y] = false;
	}
	
	//产生石头, 可以覆盖这个方法改变石头在地面上的布局
	public void generateRocks() {
		for (int x = 0; x < GlobalSet.WIDTH; x++)
			rocks[x][0] = rocks[x][GlobalSet.HEIGHT - 1] = true;
		for (int y = 0; y < GlobalSet.HEIGHT; y++)
			rocks[0][y] = rocks[GlobalSet.WIDTH - 1][y] = true;
	}
	
	//另一种产生石头的方法，使用时先清空石头, 再调用这个方法产生石头布局
	public void generateRocks2() {
		for (int y = 0; y < 6; y++) {
			rocks[0][y] = true;
			rocks[GlobalSet.WIDTH - 1][y] = true;
			rocks[0][GlobalSet.HEIGHT - 1 - y] = true;
			rocks[GlobalSet.WIDTH - 1][GlobalSet.HEIGHT - 1 - y] = true;
		}
		for (int y = 6; y < GlobalSet.HEIGHT - 6; y++) {
			rocks[6][y] = true;
			rocks[GlobalSet.WIDTH - 7][y] = true;
		}
	}	

	//初始化地面	
	public void init() {clear();generateRocks();}
	public Ground() {init();}

	//添加一块石头到指定格子坐标
	public void addRock(int x, int y) {
		rocks[x][y] = true;
	}

	//蛇是否吃到了石头?
	public boolean isSnakeEatRock(Snake snake) {
		return rocks[snake.getHead().x][snake.getHead().y];
	}

	//随机生成一个不是石头的坐标, 用于丢食物
	public Point getFreePoint() {
		do {
			freePoint.x = random.nextInt(GlobalSet.WIDTH);
			freePoint.y = random.nextInt(GlobalSet.HEIGHT);
		} while (rocks[freePoint.x][freePoint.y]);
		return freePoint;
	}

	//得到石头的颜色
	public Color getRockColor() {return rockColor;}

	//设置石头的颜色
	public void setRockColor(Color rockColor) {
		this.rockColor = rockColor;
	}

	//画自己?
	public void drawMe(Graphics g) {
		for (int x = 0; x < GlobalSet.WIDTH; x++)
			for (int y = 0; y < GlobalSet.HEIGHT; y++) {
				/* 画石头 */
				if (rocks[x][y]) {
					g.setColor(rockColor);
					drawRock(g, x * GlobalSet.CELL_WIDTH, y * GlobalSet.CELL_HEIGHT,
							GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
				} else if (drawGridding) {
					/* 画网格(如果允许) */
					g.setColor(griddingColor);
					drawGridding(g, x * GlobalSet.CELL_WIDTH, y
							* GlobalSet.CELL_HEIGHT, GlobalSet.CELL_WIDTH,
							GlobalSet.CELL_HEIGHT);
				}
			}
	}

	// 画一块石头, 可以覆盖这个方法改变石头的显示
	public void drawRock(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}

	//画网格, 可以覆盖这个方法改变网格的显示
	public void drawGridding(Graphics g, int x, int y, int width, int height) {
		g.drawRect(x, y, width, height);//绘制指定矩形的边框
	}

	//得到网格的颜色
 	Color getGriddingColor() {return griddingColor;}

	//设置网格的颜色
	public void setGriddingColor(Color griddingColor) {
		this.griddingColor = griddingColor;
	}

	//查看网格开关状态
	public boolean isDrawGridding() {return drawGridding;}

	//设置是否画网格
	public void setDrawGridding(boolean drawGridding) {
		this.drawGridding = drawGridding;
	}
}
