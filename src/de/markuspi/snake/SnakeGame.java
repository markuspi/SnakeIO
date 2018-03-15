package de.markuspi.snake;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

public class SnakeGame extends Canvas implements Runnable {
	private static final long serialVersionUID = 1812337108549320476L;

	public static final int SCALE = 12;
	public static final int WORLD_HEIGHT = 40;
	public static final int WORLD_WIDTH = 60;
	public static final Dimension SIZE = new Dimension(WORLD_WIDTH * SCALE, WORLD_HEIGHT * SCALE);

	private boolean running = true;

	private double speed = 1;
	private int generation = 1;
	private int longest = 0;

	private World[] worlds = new World[200];
	private MatingPool<Network> pool = new MatingPool<>();

	public SnakeGame() {
		setPreferredSize(SIZE);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				SnakeGame.this.keyTyped(e);
			}
		});

		for (int i = 0; i < worlds.length; i++) {
			worlds[i] = new World(WORLD_WIDTH, WORLD_HEIGHT);
		}
	}

	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case '+':
			speed *= 2D;
			break;
		case '-':
			speed /= 2D;
			break;
		case ' ':
			speed = 1;
			break;
		}
	}

	public void selection() {
		pool.clear();

		ArrayList<Snake> best = new ArrayList<>();

		for (World w : worlds) {
			best.add(w.snake);
		}

		Collections.sort(best);
		longest = best.get(0).getLength();
		for (int i = 0; i < 20; i++) {
			Snake snake = best.get(i);
			pool.add(snake.getFitness() * snake.getFitness(), snake.snakeBrain);
		}
	}

	public void reproduction() {
		generation++;
		for (int i = 0; i < worlds.length; i++) {
			Network brainA = pool.randomEntry();
			Network brainB = pool.randomEntry();
			worlds[i].reset();
			worlds[i].snake.snakeBrain = brainA.crossover(brainB, 0.003D);
		}
	}

	public boolean live() {
		boolean alldead = true;
		for (World w : worlds) {
			alldead &= w.step();
		}
		return alldead;
	}

	public void tick() {
		if (live()) {
			selection();
			reproduction();

		}

	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, SIZE.width, SIZE.height);

		g2.setColor(Color.BLACK);
		g2.drawString("Speed: " + speed + " Gen: " + generation + " Best: " + longest, 10, 10);

		// world.render(g2, 10);

		for (World w : worlds) {
			w.render(g2, SCALE);
		}

		g2.dispose();
		bs.show();
	}

	@Override
	public void run() {
		double UPS = 120;
		double FPS = 60;

		double timeU = 1E9 / UPS;
		final double timeF = 1E9 / FPS;
		double deltaU = 0, deltaF = 0;
		long lastTime = System.nanoTime();
		long now;

		Toolkit toolkit = Toolkit.getDefaultToolkit(); /* get AWT toolkit */

		while (running) {
			now = System.nanoTime();
			if (speed < 1) {
				timeU = 1E9 / (UPS * speed);
			} else {
				timeU = 1E9 / UPS;
			}
			deltaU += (now - lastTime) / timeU;
			deltaF += (now - lastTime) / timeF;
			lastTime = now;

			if (deltaU >= 1) {
				for (int i = 0; i < speed; i++) {
					tick();
				}
				deltaU--;

				if (deltaU > 5) {
					deltaU %= 2;
				}
			}

			if (deltaF >= 1) {
				render();
				deltaF--;
				toolkit.sync();

				if (deltaF > 10) {
					deltaF %= 2;
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SnakeGame sg = new SnakeGame();

		JFrame frame = new JFrame("Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(sg, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		Thread gameThread = new Thread(sg);
		gameThread.start();

	}

}
