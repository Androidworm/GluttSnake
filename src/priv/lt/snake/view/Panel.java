package priv.lt.snake.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import priv.lt.snake.entities.Food;
import priv.lt.snake.entities.Ground;
import priv.lt.snake.entities.Snake;
import priv.lt.snake.util.GlobalSet;

//��Ϸ����ʾ����
public class Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Image oimg;
	private Graphics og;

	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0xcfcfcf);// ������ɫ,Ĭ������ɫ
	private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;

	public Panel() {
		/* ���ô�С�Ͳ��� */
		this.setSize(GlobalSet.WIDTH * GlobalSet.CELL_WIDTH, GlobalSet.HEIGHT * GlobalSet.CELL_HEIGHT);
		this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));// LOWERED�ֶα�ʾ���̸������͡�
		this.setFocusable(true);//����״̬����,����һ��button���ͻ���һ�����߿��ڰ�ť��
	}

	// ������ʾ Ground, Shape
	// synchronized��������һ����������һ��������ʱ���ܹ���֤��ͬһʱ�����ֻ��һ���߳�ִ�иöδ��롣
	public synchronized void redisplay(Ground ground, Snake snake, Food food) {
		if (og == null) {
			oimg = createImage(getSize().width, getSize().height);
			if (oimg != null)
				og = oimg.getGraphics();
		}
		if (og != null) {
			og.setColor(backgroundColor);
			og.fillRect(0, 0, GlobalSet.WIDTH * GlobalSet.CELL_WIDTH, GlobalSet.HEIGHT * GlobalSet.CELL_HEIGHT);//���ָ���ľ���
			if (ground != null)
				ground.drawMe(og);
			snake.drawMe(og);
			if (food != null)
				food.drawMe(og);
			this.paint(this.getGraphics());
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(oimg, 0, 0, this);//���ͼ���Ѿ��������أ����������ز��ٷ������ģ��� drawImage����true�����򣬷��� false
	}

	// �õ���ǰ�ı�����ɫ
	public Color getBackgroundColor() {return backgroundColor;}

	// ���õ�ǰ�ı�����ɫ
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
