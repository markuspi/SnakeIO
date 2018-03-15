package de.markuspi.snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class World implements KeyListener {

	public static final int[][] LOOK = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
			{ 1, -1 } };

	public int width;
	public int height;

	public Point2D food;

	public Snake snake;

	private Color col;

	private Random rnd = new Random();

	public World(int width, int height) {
		this.width = width;
		this.height = height;

		reset();

		snake.snakeBrain = new Network(24, 24, 4);

		col = Color.getHSBColor((float) Math.random(), 1F, 1F);

		moveFood();
	}

	public void reset() {
		this.snake = new Snake(new Point2D(width / 2, height / 2), 5);
	}

	public void moveFood() {
		int foodX = 5 + rnd.nextInt(width - 10);
		int foodY = 5 + rnd.nextInt(height - 10);
		this.food = new Point2D(foodX, foodY);
	}

	public void render(Graphics2D g2, int scale) {

		if (snake.isDead())
			return;
		g2.setColor(col);
		snake.render(g2, scale);

		g2.drawRect(food.x * scale + 1, food.y * scale + 1, scale - 2, scale - 2);
	}

	public boolean step() {
		Point2D head = snake.getHeadPos();

		double[] values = new double[8 * 3];
		for (int i = 0; i < LOOK.length; i++) {
			int lx = LOOK[i][0];
			int ly = LOOK[i][1];

			int x = head.x;
			int y = head.y;

			int dist = 0;
			while (x >= 0 && y >= 0 && x < width && y < height) {
				dist++;
				x += lx;
				y += ly;

				if (x == food.x && y == food.y) {
					values[i * 3 + 0] = 1D - 0.05 * dist;
				}

				// if (snake.contains(new Point2D(x, y)) && values[i*3+1] == 0) {
				// values[i*3+1] = 1D - 0.05 * dist;
				// }
			}
			values[i * 3 + 2] = 1D - 0.05 * dist;
		}

		// System.out.println(Arrays.toString(values));
		Direction choice = Direction.values()[snake.snakeBrain.decide(values)];
		// System.out.println("Choice: " + choice);
		snake.setDirection(choice);
		snake.step(this);

		if (snake.getHeadPos().equals(food)) {
			System.out.println("Yum");
			moveFood();
			snake.addLength(1);
		}

		return snake.isDead();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
			snake.setDirection(Direction.UP);
			break;
		case 'a':
			snake.setDirection(Direction.LEFT);
			break;
		case 's':
			snake.setDirection(Direction.DOWN);
			break;
		case 'd':
			snake.setDirection(Direction.RIGHT);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'r') {
			reset();
			snake.snakeBrain = new Network(24, 16, 4);
		}
	}

}
