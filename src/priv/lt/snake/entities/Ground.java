package priv.lt.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import priv.lt.snake.util.GlobalSet;

// ����
// ����ͨ��setRockColor(), setGriddingColor()��������ʯͷ���������ɫ
// ͨ��setDrawGridding() ���������Ƿ�����
// ���Ը��� drawRock�����Ըı�ʯͷ����ʾ��ʽ
//  ����ͨ������genernateRocks() �����ı�ʯͷ�����Ĳ���
public class Ground {

	//���ʯͷ�Ķ�ά���� 
	private boolean rocks[][] = new boolean[GlobalSet.WIDTH][GlobalSet.HEIGHT];
	//���getFreePoint()�������ɵĲ���ʯͷ����������� 
	private Point freePoint = new Point();
	public static final Color DEFAULT_ROCK_COLOR = new Color(0x666666);//Ĭ�ϻ�ɫʯͷ
	private Color rockColor = DEFAULT_ROCK_COLOR;
	public static final Color DEFAULT_GRIDDING_COLOR = Color.LIGHT_GRAY;//Ĭ������ɫ����
	private Color griddingColor = DEFAULT_GRIDDING_COLOR;	
	private Random random = new Random();
	private boolean drawGridding = false;//���񿪹أ�Ĭ�Ϲ�

    //���ʯͷ
	public void clear() {
		for (int x = 0; x < GlobalSet.WIDTH; x++)
			for (int y = 0; y < GlobalSet.HEIGHT; y++)
				rocks[x][y] = false;
	}
	
	//����ʯͷ, ���Ը�����������ı�ʯͷ�ڵ����ϵĲ���
	public void generateRocks() {
		for (int x = 0; x < GlobalSet.WIDTH; x++)
			rocks[x][0] = rocks[x][GlobalSet.HEIGHT - 1] = true;
		for (int y = 0; y < GlobalSet.HEIGHT; y++)
			rocks[0][y] = rocks[GlobalSet.WIDTH - 1][y] = true;
	}
	
	//��һ�ֲ���ʯͷ�ķ�����ʹ��ʱ�����ʯͷ, �ٵ��������������ʯͷ����
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

	//��ʼ������	
	public void init() {clear();generateRocks();}
	public Ground() {init();}

	//���һ��ʯͷ��ָ����������
	public void addRock(int x, int y) {
		rocks[x][y] = true;
	}

	//���Ƿ�Ե���ʯͷ?
	public boolean isSnakeEatRock(Snake snake) {
		return rocks[snake.getHead().x][snake.getHead().y];
	}

	//�������һ������ʯͷ������, ���ڶ�ʳ��
	public Point getFreePoint() {
		do {
			freePoint.x = random.nextInt(GlobalSet.WIDTH);
			freePoint.y = random.nextInt(GlobalSet.HEIGHT);
		} while (rocks[freePoint.x][freePoint.y]);
		return freePoint;
	}

	//�õ�ʯͷ����ɫ
	public Color getRockColor() {return rockColor;}

	//����ʯͷ����ɫ
	public void setRockColor(Color rockColor) {
		this.rockColor = rockColor;
	}

	//���Լ�?
	public void drawMe(Graphics g) {
		for (int x = 0; x < GlobalSet.WIDTH; x++)
			for (int y = 0; y < GlobalSet.HEIGHT; y++) {
				/* ��ʯͷ */
				if (rocks[x][y]) {
					g.setColor(rockColor);
					drawRock(g, x * GlobalSet.CELL_WIDTH, y * GlobalSet.CELL_HEIGHT,
							GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
				} else if (drawGridding) {
					/* ������(�������) */
					g.setColor(griddingColor);
					drawGridding(g, x * GlobalSet.CELL_WIDTH, y
							* GlobalSet.CELL_HEIGHT, GlobalSet.CELL_WIDTH,
							GlobalSet.CELL_HEIGHT);
				}
			}
	}

	// ��һ��ʯͷ, ���Ը�����������ı�ʯͷ����ʾ
	public void drawRock(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}

	//������, ���Ը�����������ı��������ʾ
	public void drawGridding(Graphics g, int x, int y, int width, int height) {
		g.drawRect(x, y, width, height);//����ָ�����εı߿�
	}

	//�õ��������ɫ
 	Color getGriddingColor() {return griddingColor;}

	//�����������ɫ
	public void setGriddingColor(Color griddingColor) {
		this.griddingColor = griddingColor;
	}

	//�鿴���񿪹�״̬
	public boolean isDrawGridding() {return drawGridding;}

	//�����Ƿ�����
	public void setDrawGridding(boolean drawGridding) {
		this.drawGridding = drawGridding;
	}
}
