package priv.lt.snake.listener;

/**
 * 
 * 蛇的监听器
 * 
 */
public interface SnakeListener {
	/**
	 * 蛇移动事件
	 */
	void snakeMoved();

	/**
	 * 蛇吃到食物事件
	 */
	void snakeEatFood();
}
