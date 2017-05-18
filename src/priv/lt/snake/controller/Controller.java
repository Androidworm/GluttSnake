package priv.lt.snake.controller;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JLabel;

import priv.lt.snake.entities.Food;
import priv.lt.snake.entities.Ground;
import priv.lt.snake.entities.Snake;
import priv.lt.snake.listener.GameListener;
import priv.lt.snake.listener.SnakeListener;
import priv.lt.snake.util.GlobalSet;
import priv.lt.snake.view.Panel;


public class Controller extends KeyAdapter implements SnakeListener {
    //KeyAdapter的继承，为了方便创建监听器对象
	private Ground ground;
	private Snake snake;
	private Food food;
	private Panel gamePanel;

	/* 提示信息 */
	private JLabel gameInfoLabel;
	private boolean playing;
	private int map;

	/* 控制器监听器 */
	private Set<GameListener> listeners = new HashSet<GameListener>();

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() != KeyEvent.VK_Y && !playing) return;
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_UP:
				if (snake.isPause())
				{
					snake.changePause();			
					for (GameListener l : listeners)l.gameContinue();
				}
				snake.changeDirection(Snake.UP);
				break;
			case KeyEvent.VK_DOWN:
				if (snake.isPause())
				{
					snake.changePause();
					for (GameListener l : listeners)l.gameContinue();
				}
				snake.changeDirection(Snake.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				if (snake.isPause()) 
				{
					snake.changePause();
					for (GameListener l : listeners)l.gameContinue();
				}
				snake.changeDirection(Snake.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				if (snake.isPause())
				{
					snake.changePause();
					for (GameListener l : listeners)l.gameContinue();
				}
				snake.changeDirection(Snake.RIGHT);
				break;
				/* 回车或空格 (暂停) */
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_SPACE:
			snake.changePause();
			/* === */
			for (GameListener l : listeners)
				if (snake.isPause())	l.gamePause();
				else	l.gameContinue();
				break;
				/* PAGE_UP 加速 */
			case KeyEvent.VK_PAGE_UP:snake.speedUp();break;
			/* PAGE_DOWN 减速 */
			case KeyEvent.VK_PAGE_DOWN:snake.speedDown();break;
			/* 字母键 Y (重新开始游戏) */
			case KeyEvent.VK_Y:if (!isPlaying())newGame();break;
		}
		/* 重新显示 */
		if (gamePanel != null)gamePanel.redisplay(ground, snake, food);
		/* 更新提示 */
		if (gameInfoLabel != null)gameInfoLabel.setText(getNewInfo());
	}

	public void snakeMoved() {
		/* 判断是否吃到食物 */
		if (food != null && food.isSnakeEatFood(snake)) {
			/* 吃到食物后, 蛇增加身体, 再重新丢一个食物 */
			snake.eatFood();
			food.setLocation(ground == null ? food.getNew() : ground
					.getFreePoint());

		}/* 如果吃到食物, 就肯定不会吃到石头 */
		else if (ground != null && ground.isSnakeEatRock(snake)) {
			/* 如果吃到的是石头, 或吃到自己的身体, 就让蛇死掉 */
			stopGame();
		}
		if (snake.isEatBody())
			stopGame();
		if (gamePanel != null)
			gamePanel.redisplay(ground, snake, food);
		/* 更新提示 */
		if (gameInfoLabel != null)
			gameInfoLabel.setText(getNewInfo());
	}

	/**
	 * 开始一个新游戏
	 */
	public void newGame() {
		if (ground != null) {
			switch (map)
			{
				case 2:ground.clear();ground.generateRocks2();break;
				default:ground.init();break;
			}
		}
		playing = true;
		snake.reNew();		
		for (GameListener l : listeners)
			l.gameStart();
	}

	public void stopGame() {
		if (playing) {
			playing = false;
			snake.dead();
			for (GameListener l : listeners)
				l.gameOver();
		}
	}

	/**
	 * 暂停游戏
	 */
	public void pauseGame() {
		snake.setPause(true);
		for (GameListener l : listeners)
			l.gamePause();
	}

	/**
	 * 继续游戏
	 */
	public void continueGame() {
		snake.setPause(false);
		for (GameListener l : listeners)
			l.gameContinue();
	}

	/*接受Snake,Food,Ground 的构造器*/
	public Controller(Snake snake, Food food, Ground ground, Panel gamePanel) {
		this.snake = snake;
		this.food = food;
		this.ground = ground;
		this.gamePanel = gamePanel;
		/* 先丢一个食物 */
		if (ground != null && food != null)
			food.setLocation(ground.getFreePoint());
		/* 注册监听器 */
		this.snake.addSnakeListener(this);
	}

	/*多接受一个显示提示信息的JLabel*/
	public Controller(Snake snake, Food food, Ground ground,Panel gamePanel, JLabel gameInfoLabel){
		this(snake, food, ground, gamePanel);
		this.setGameInfoLabel(gameInfoLabel);

		if (gameInfoLabel != null)
			gameInfoLabel.setText(getNewInfo());
	}

	/*得到最新的提示信息*/
	public String getNewInfo() {
		if (!snake.isLive())
			return " ";// " 提示: 按 Y 开始新游戏";
		else
			return new StringBuffer().append("提示: ").append("速度 ").append(
					snake.getSpeed()).toString()
					+ " 毫秒/格";
	}

	/**
	 * 添加监听器
	 * 
	 * 
	 */
	public synchronized void addGameListener(GameListener l) {
		if (l != null)
			this.listeners.add(l);
	}

	/**
	 * 移除监听器
	 * 
	 * 
	 */
	public synchronized void removeGameListener(GameListener l) {
		if (l != null)
			this.listeners.remove(l);
	}

	/**
	 * 得到蛇的引用
	 * 
	 * 
	 */
	public Snake getSnake() {
		return this.snake;
	}

	/**
	 * 得到食物的引用
	 * 
	 * 
	 */
	public Food getFood() {
		return this.food;
	}

	/**
	 * 得到地形的引用
	 * 
	 * @return
	 */
	public Ground getGround() {
		return this.ground;
	}

	/**
	 * 处理蛇吃到食物后触发的 snakeEatFood事件, 但什么也没做
	 * 可以覆盖这个方法增加功能, 例如, 增加记分功能
	 */
	public void snakeEatFood() {
		// TODO Auto-generated method stub
		System.out.println("吃到食物!");
	}

	public Panel getGamePanel() {
		return gamePanel;
	}

	/*设置GamePanel */
	public void setGamePanel(Panel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public JLabel getGameInfoLabel() {
		return gameInfoLabel;
	}

	public void setGameInfoLabel(JLabel gameInfoLabel) {
		this.gameInfoLabel = gameInfoLabel;
		this.gameInfoLabel.setSize(GlobalSet.WIDTH * GlobalSet.CELL_WIDTH, 20);
		this.gameInfoLabel.setFont(new Font("宋体", Font.PLAIN, 12));
		gameInfoLabel.setText(this.getNewInfo());
	}

	public void setGround(Ground ground) {
		this.ground = ground;
	}

	public void setSnake(Snake snake) {
		this.snake = snake;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isPausingGame() {
		// TODO Auto-generated method stub
		return snake.isPause();
	}

}
