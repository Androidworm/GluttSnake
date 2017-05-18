package priv.lt.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import priv.lt.snake.util.GlobalSet;

//ʳ��, ��x , y ���� �� ��ɫ������
//������setColor() �ı�ʳ�����ɫ
//Ҳ����ͨ������ drawFood���� �ı�ʳ�����ʾ��ʽ
public class Food extends Point {

	/*ΪʲôҪ��ʽ������һ��SerialVersionUID����Ϊ������޸�������ࣨ���Ժͷ�������
	 * ��ô�㵱ǰ���Ĭ��SerialVersionUID�ͻ�ı䣬
	 * ������ǰ���л������ص�SerialVersionUID��ͬ����ô����ʱ�����оͻ����*/
	private static final long serialVersionUID = 1L;

	private Color color = new Color(0xcc0033);//Ĭ��ʳ���ɫ
	private Random random = new Random();//java.util;
	public Food() {super();}//���ø���ķ���,Ҳ����Point�ࣿ

	//��������㣬Ҳ����ʳ������
	public Point getNew() {
		Point p = new Point();
		p.x = random.nextInt(GlobalSet.WIDTH);
		p.y = random.nextInt(GlobalSet.HEIGHT);
		return p;
	}

	public Food(Point p) {super(p);}//���캯������ʼ��ʳ��

	//�ж����Ƿ�Ե�ʳ�
	public boolean isSnakeEatFood(Snake snake) {
		return this.equals(snake.getHead());
	}

   //���Լ�, ������ drawFood����?
	public void drawMe(Graphics g) {
		g.setColor(color);
		drawFood(g, x * GlobalSet.CELL_WIDTH, y * GlobalSet.CELL_HEIGHT,GlobalSet.CELL_WIDTH, GlobalSet.CELL_HEIGHT);
	}

	// ��ʳ��, ���Ը�����������ı�ʳ�����ʾ
	public void drawFood(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}

	//�õ�ʳ�����ɫ
	public Color getColor() {return color;}

	//����ʳ�����ɫ
	public void setColor(Color color) {
		this.color = color;
	}
}
