package priv.lt.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import priv.lt.snake.util.GlobalSet;

//食物, 有x , y 坐标 和 颜色等属性
//可以用setColor() 改变食物的颜色
//也可以通过覆盖 drawFood方法 改变食物的显示方式
public class Food extends Point {

	/*为什么要显式的声明一个SerialVersionUID，因为当你的修改了你的类（属性和方法），
	 * 那么你当前类的默认SerialVersionUID就会改变，
	 * 和你以前序列化到本地的SerialVersionUID不同，那么你这时候反序列就会出错！*/
	private static final long serialVersionUID = 1L;

	private Color color = new Color(0xcc0033);//默认食物红色
	private Random random = new Random();//java.util;
	public Food() {super();}//调用父类的方法,也就是Point类？

	//生成随机点，也就是食物坐标
	public Point getNew() {
		Point p = new Point();
		p.x = random.nextInt(GlobalSet.WIDTH);
		p.y = random.nextInt(GlobalSet.HEIGHT);
		return p;
	}

	public Food(Point p) {super(p);}//构造函数，初始化食物

	//判断蛇是否吃到食物？
	public boolean isSnakeEatFood(Snake snake) {
		return this.equals(snake.getHead());
	}

   //画自己, 将调用 drawFood方法?
	public void drawMe(Graphics g) {
		g.setColor(color);
		drawFood(g, x * GlobalSet.CELL_WIDTH, y * GlobalSet.CELL_HEIGHT,GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
	}

	// 画食物, 可以覆盖这个方法改变食物的显示
	public void drawFood(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}

	//得到食物的颜色
	public Color getColor() {return color;}

	//设置食物的颜色
	public void setColor(Color color) {
		this.color = color;
	}
}
