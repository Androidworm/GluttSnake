package priv.lt.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import priv.lt.snake.listener.SnakeListener;
import priv.lt.snake.util.GlobalSet;

//��
//move()����Ĭ��֧���ߵ����Ժ����һ�߳���
//Ҳ����ͨ������ drawHead���� �ı���ͷ����ʾ��ʽ�͸���drawBody�����ı����������ʾ��ʽ
//���ڲ���MoveDriver �����߶�ʱ�ƶ�
//begin() �����ڲ�����һ���µ��߳������߶�ʱ�ƶ�, �������������ʱ��Ҫע��
//�ߵ�����ĳ�ʼ���ȱ�����ڵ���2
public class Snake {
	public static final int UP = 1;
	public static final int DOWN = -1;
	public static final int LEFT = 2;
	public static final int RIGHT = -2;
	//��(����ڵ�)
	private LinkedList<Point> body = new LinkedList<Point>();
	
	private int oldDirection;//��һ�ε��ƶ�����  	 
	private int newDirection;//��һ���ķ���(��Ч����)	 
	private Point head;//��ʱ�����ͷ������	
	private Point tail;//��ʱ�����β�͵����� 	
	private int speed;//�ƶ��ٶ� 	
	private boolean live;//����, �Ƿ���� 	
	private boolean pause;//�Ƿ���ͣ 
	
	private Set<SnakeListener> listeners = new HashSet<SnakeListener>();
	
	public static final Color DEFAULT_HEAD_COLOR = new Color(0x3366FF);//��ͷ����ɫ ,Ĭ����ɫ
	private Color headColor = DEFAULT_HEAD_COLOR;
	public static final Color DEFAULT_BODY_COLOR = new Color(0xcc0033);//���������ɫ��Ĭ�Ϻ�ɫ
	private Color bodyColor = DEFAULT_BODY_COLOR;

	public void move() {
		//�����෴���� 
		if (oldDirection + newDirection != 0)oldDirection = newDirection;
		//����β���ó�����������������Ϊ����ͷ ,getLocation������һ���µ�Point,tail��β�����걣������, �Ե�ʳ��ʱ�ټ���
		tail = (head = takeTail()).getLocation();
		head.setLocation(getHead());//������ͷ�������� ��������
		//���ݷ��������ƶ�
		switch (oldDirection)
		{
			case UP:
				head.y--;
				if (head.y < 0)head.y = GlobalSet.HEIGHT - 1;//�������˿��Դ���һ�߳���
				break;
			case DOWN:
				head.y++;			
				if (head.y == GlobalSet.HEIGHT)head.y = 0;//�������˿��Դ���һ�߳���
				break;
			case LEFT:
				head.x--;
				if (head.x < 0)head.x = GlobalSet.WIDTH - 1;//�������˿��Դ���һ�߳���
				break;
			case RIGHT:
				head.x++;
				if (head.x == GlobalSet.WIDTH)head.x = 0;//�������˿��Դ���һ�߳���
				break;
		}
		//��ӵ�ͷ��ȥ 
		body.addFirst(head);
	}

	// һ���ڲ���, �����߶�ʱ�ƶ�
	private class SnakeDriver implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			while (live) {
				if (!pause) {
					move();
					/* ���� ControllerListener ��״̬�ı��¼� */
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

	//��β��������һ���ڵ�
	public void eatFood() {
		//����һ���ƶ��õ��Ľڵ��ټ���
		body.addLast(tail.getLocation());
		//����SnakeListener �� snakeEatFood �¼�
		for (SnakeListener l : listeners)
			l.snakeEatFood();
	}

	//�ı䷽��
	public void changeDirection(int direction) {this.newDirection = direction;}

	//�õ���ͷ�ڵ�,�Լ�Լ���ĸ�����ͷ
	public Point getHead() {return body.getFirst();}

	//�õ���β�ͽڵ�,ȥ����β��
	public Point takeTail() {return body.removeLast();}

	//�õ��ߵĳ���
	public int getLength() {return body.size();}

	// ���߿�ʼ�˶�,����һ���µ��߳�
	public void begin() {new Thread(new SnakeDriver()).start();}

    //��ʼ���ߵ���Ϣ,����, λ��, ����, �ٶ�, ��������ͣ״̬
	public void init() {
		body.clear();
		int x = GlobalSet.WIDTH / 2 - GlobalSet.INIT_LENGTH / 2;//��ʼ��λ�����м� 
		int y = GlobalSet.HEIGHT / 2;
		for (int i = 0; i < GlobalSet.INIT_LENGTH; i++)
			this.body.addFirst(new Point(x++, y));//��β�Ϳ�ʼ�ӣ�һ��һ��
		oldDirection = newDirection = RIGHT;//����Ĭ�Ϸ���Ϊ����
		speed = GlobalSet.SPEED;//��ʼ���ٶ�
		live = true;//��ʼ������
		pause = false;//��ͣ״̬
	}
	
	//���߸���, ����ʼ�˶�
	public void reNew() {
		init();
		begin();
	}

    //�Ƿ�Ե��Լ�������
	public boolean isEatBody() {
		/* Ҫ����ͷ�ų�, body.get(0) ����ͷ */
		for (int i = 1; i < body.size(); i++)
			if (getHead().equals(body.get(i)))
				return true;
		return false;
	}

	//����ͷ, ���Ը�����������ı���ͷ����ʾ
	public void drawHead(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}

	//���ߵ�һ������, ���Ը�����������ı��ߵ�����ڵ����ʾ
	public void drawBody(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}
	
	//���Լ�
	public void drawMe(Graphics g) {
		for (Point p : body) {
			g.setColor(bodyColor);
			drawBody(g, p.x * GlobalSet.CELL_WIDTH, p.y * GlobalSet.CELL_HEIGHT,GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
		}
		g.setColor(headColor);
		drawHead(g, getHead().x * GlobalSet.CELL_WIDTH, getHead().y* GlobalSet.CELL_HEIGHT, GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
	}

	//�õ���ͷ����ɫ
	public Color getHeadColor() {return headColor;}

	//������ͷ����ɫ
	public void setHeadColor(Color headColor) {this.headColor = headColor;}

	//�õ����������ɫ
	public Color getBodyColor() {return bodyColor;}

	//�������������ɫ
	public void setBodyColor(Color bodyColor) {this.bodyColor = bodyColor;}

	//��Ӽ�����
	public synchronized void addSnakeListener(SnakeListener l) {
		if (l == null)return;
		this.listeners.add(l);
	}

	//�Ƴ�������
	public synchronized void removeSnakeListener(SnakeListener l) {
		if (l == null)return;
		this.listeners.remove(l);
	}

	//����, ����Ϊ Global �����õ� SPEED_STEP 
	public void speedUp() {
		if (speed > GlobalSet.SPEED_STEP)
			speed -= GlobalSet.SPEED_STEP;////��Ϊspeed��sleep��˯��ʱ�䣬����ֵԽС�ٶ�Խ��
	}

	//����
	public void speedDown() {speed += GlobalSet.SPEED_STEP;}

	//�õ��ߵ��ƶ��ٶ�
	public int getSpeed() {return speed;}

	//�����ߵ��ƶ��ٶ�
	public void setSpeed(int speed) {this.speed = speed;}

	//���Ƿ�������
	public boolean isLive() {return live;}

	//�������Ƿ�����
	public void setLive(boolean live) {this.live = live;}

	//����������
	public void dead() {this.live = false;}

	//�Ƿ�����ͣ״̬
	public boolean isPause() {return pause;}

	//������ͣ״̬
	public void setPause(boolean pause) {this.pause = pause;}

	//������ͣ״̬
	public void changePause() {pause = !pause;}
}