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

//游戏的显示界面
public class Panel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Image oimg;
	private Graphics og;

	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0xcfcfcf);// 背景颜色,默认亮灰色
	private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;

	public Panel() {
		/* 设置大小和布局 */
		this.setSize(GlobalSet.WIDTH * GlobalSet.CELL_WIDTH, GlobalSet.HEIGHT * GlobalSet.CELL_HEIGHT);
		this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));// LOWERED字段表示阴刻浮雕化类型。
		this.setFocusable(true);//焦点状态设置,例如一个button，就会有一个虚线框在按钮中
	}

	// 重新显示 Ground, Shape
	// synchronized用来修饰一个方法或者一个代码块的时候，能够保证在同一时刻最多只有一个线程执行该段代码。
	public synchronized void redisplay(Ground ground, Snake snake, Food food) {
		if (og == null) {
			oimg = createImage(getSize().width, getSize().height);
			if (oimg != null)
				og = oimg.getGraphics();
		}
		if (og != null) {
			og.setColor(backgroundColor);
			og.fillRect(0, 0, GlobalSet.WIDTH * GlobalSet.CELL_WIDTH, GlobalSet.HEIGHT * GlobalSet.CELL_HEIGHT);//填充指定的矩形
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
		g.drawImage(oimg, 0, 0, this);//如果图像已经完整加载，并且其像素不再发生更改，则 drawImage返回true。否则，返回 false
	}

	// 得到当前的背景颜色
	public Color getBackgroundColor() {return backgroundColor;}

	// 设置当前的背景颜色
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
