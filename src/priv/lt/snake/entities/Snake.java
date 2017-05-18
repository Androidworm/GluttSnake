package priv.lt.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import priv.lt.snake.listener.SnakeListener;
import priv.lt.snake.util.GlobalSet;

//蛇
//move()方法默认支持走到边以后从另一边出现
//也可以通过覆盖 drawHead方法 改变蛇头的显示方式和覆盖drawBody方法改变蛇身体的显示方式
//用内部类MoveDriver 驱动蛇定时移动
//begin() 方法内部开启一个新的线程驱动蛇定时移动, 调用这个方法的时候要注意
//蛇的身体的初始长度必须大于等于2
public class Snake {
	public static final int UP = 1;
	public static final int DOWN = -1;
	public static final int LEFT = 2;
	public static final int RIGHT = -2;
	//蛇(多个节点)
	private LinkedList<Point> body = new LinkedList<Point>();
	
	private int oldDirection;//上一次的移动方向  	 
	private int newDirection;//下一步的方向(有效方向)	 
	private Point head;//临时存放蛇头的坐标	
	private Point tail;//临时存放蛇尾巴的坐标 	
	private int speed;//移动速度 	
	private boolean live;//生命, 是否活着 	
	private boolean pause;//是否暂停 
	
	private Set<SnakeListener> listeners = new HashSet<SnakeListener>();
	
	public static final Color DEFAULT_HEAD_COLOR = new Color(0x3366FF);//蛇头的颜色 ,默认蓝色
	private Color headColor = DEFAULT_HEAD_COLOR;
	public static final Color DEFAULT_BODY_COLOR = new Color(0xcc0033);//蛇身体的颜色，默认红色
	private Color bodyColor = DEFAULT_BODY_COLOR;

	public void move() {
		//忽略相反方向 
		if (oldDirection + newDirection != 0)oldDirection = newDirection;
		//把蛇尾巴拿出来重新设置坐标作为新蛇头 ,getLocation将返回一个新的Point,tail把尾巴坐标保存下来, 吃到食物时再加上
		tail = (head = takeTail()).getLocation();
		head.setLocation(getHead());//根据蛇头的坐标再 上下左右
		//根据方向让蛇移动
		switch (oldDirection)
		{
			case UP:
				head.y--;
				if (head.y < 0)head.y = GlobalSet.HEIGHT - 1;//到边上了可以从另一边出现
				break;
			case DOWN:
				head.y++;			
				if (head.y == GlobalSet.HEIGHT)head.y = 0;//到边上了可以从另一边出现
				break;
			case LEFT:
				head.x--;
				if (head.x < 0)head.x = GlobalSet.WIDTH - 1;//到边上了可以从另一边出现
				break;
			case RIGHT:
				head.x++;
				if (head.x == GlobalSet.WIDTH)head.x = 0;//到边上了可以从另一边出现
				break;
		}
		//添加到头上去 
		body.addFirst(head);
	}

	// 一个内部类, 驱动蛇定时移动
	private class SnakeDriver implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			while (live) {
				if (!pause) {
					move();
					/* 触发 ControllerListener 的状态改变事件 */
					for (SnakeListener l : listeners)
						l.snakeMoved();
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//在尾巴上增加一个节点
	public void eatFood() {
		//把上一次移动拿掉的节点再加上
		body.addLast(tail.getLocation());
		//触发SnakeListener 的 snakeEatFood 事件
		for (SnakeListener l : listeners)
			l.snakeEatFood();
	}

	//改变方向
	public void changeDirection(int direction) {this.newDirection = direction;}

	//得到蛇头节点,自己约定哪个是蛇头
	public Point getHead() {return body.getFirst();}

	//拿掉蛇尾巴节点,去掉蛇尾巴
	public Point takeTail() {return body.removeLast();}

	//得到蛇的长度
	public int getLength() {return body.size();}

	// 让蛇开始运动,开启一个新的线程
	public void begin() {new Thread(new SnakeDriver()).start();}

    //初始化蛇的信息,长度, 位置, 方向, 速度, 生命和暂停状态
	public void init() {
		body.clear();
		int x = GlobalSet.WIDTH / 2 - GlobalSet.INIT_LENGTH / 2;//初始化位置在中间 
		int y = GlobalSet.HEIGHT / 2;
		for (int i = 0; i < GlobalSet.INIT_LENGTH; i++)
			this.body.addFirst(new Point(x++, y));//从尾巴开始加，一个一个
		oldDirection = newDirection = RIGHT;//设置默认方向为向右
		speed = GlobalSet.SPEED;//初始化速度
		live = true;//初始化生命
		pause = false;//暂停状态
	}
	
	//让蛇复活, 并开始运动
	public void reNew() {
		init();
		begin();
	}

    //是否吃到自己的身体
	public boolean isEatBody() {
		/* 要把蛇头排除, body.get(0) 是蛇头 */
		for (int i = 1; i < body.size(); i++)
			if (getHead().equals(body.get(i)))
				return true;
		return false;
	}

	//画蛇头, 可以覆盖这个方法改变蛇头的显示
	public void drawHead(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}

	//画蛇的一节身体, 可以覆盖这个方法改变蛇的身体节点的显示
	public void drawBody(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}
	
	//画自己
	public void drawMe(Graphics g) {
		for (Point p : body) {
			g.setColor(bodyColor);
			drawBody(g, p.x * GlobalSet.CELL_WIDTH, p.y * GlobalSet.CELL_HEIGHT,GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
		}
		g.setColor(headColor);
		drawHead(g, getHead().x * GlobalSet.CELL_WIDTH, getHead().y* GlobalSet.CELL_HEIGHT, GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
	}

	//得到蛇头的颜色
	public Color getHeadColor() {return headColor;}

	//设置蛇头的颜色
	public void setHeadColor(Color headColor) {this.headColor = headColor;}

	//得到蛇身体的颜色
	public Color getBodyColor() {return bodyColor;}

	//设置蛇身体的颜色
	public void setBodyColor(Color bodyColor) {this.bodyColor = bodyColor;}

	//添加监听器
	public synchronized void addSnakeListener(SnakeListener l) {
		if (l == null)return;
		this.listeners.add(l);
	}

	//移除监听器
	public synchronized void removeSnakeListener(SnakeListener l) {
		if (l == null)return;
		this.listeners.remove(l);
	}

	//加速, 幅度为 Global 中设置的 SPEED_STEP 
	public void speedUp() {
		if (speed > GlobalSet.SPEED_STEP)
			speed -= GlobalSet.SPEED_STEP;////因为speed是sleep中睡的时间，所以值越小速度越快
	}

	//减速
	public void speedDown() {speed += GlobalSet.SPEED_STEP;}

	//得到蛇的移动速度
	public int getSpeed() {return speed;}

	//设置蛇的移动速度
	public void setSpeed(int speed) {this.speed = speed;}

	//蛇是否死掉了
	public boolean isLive() {return live;}

	//设置蛇是否死掉
	public void setLive(boolean live) {this.live = live;}

	//设置蛇死掉
	public void dead() {this.live = false;}

	//是否是暂停状态
	public boolean isPause() {return pause;}

	//设置暂停状态
	public void setPause(boolean pause) {this.pause = pause;}

	//更改暂停状态
	public void changePause() {pause = !pause;}
}