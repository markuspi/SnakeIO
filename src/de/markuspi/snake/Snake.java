package de.markuspi.snake;

import java.awt.Graphics2D;
import java.util.LinkedList;

public class Snake implements Comparable<Snake> {

	private LinkedList<Point2D> body;
	private int extraLength;
	private Direction direction = Direction.UP;
	private Direction lastDirection = Direction.UP;
	private boolean dead = false;

	private double fitness = 0;

	private int starvation = 0;

	public Network snakeBrain;

	public Snake(Point2D head, int length) {
		body = new LinkedList<>();
		body.add(head);
		extraLength = length;

	}

	public Direction getDirection() {
		return direction;
	}

	public Point2D getHeadPos() {
		return body.getFirst();
	}

	public double getFitness() {
		// if (body.size() >= 8) {
		// return fitness * fitness;
		// }
		return body.size() * body.size();
	}

	public int getLength() {
		return body.size();
	}

	public void setDirection(Direction direction) {
		if (!lastDirection.isOpposite(direction))
			this.direction = direction;

	}

	public boolean contains(Point2D pt) {
		return body.contains(pt);
	}

	public void addLength(int len) {
		extraLength += len;
		starvation = 0;
	}

	public boolean isDead() {
		return dead;
	}

	public void die() {
		// System.out.println("Died with fitness: " + fitness + " at: " + getHeadPos());
		dead = true;
	}

	public void step(World world) {
		if (dead)
			return;

		starvation += 1;

		if (starvation > Math.max(body.size(), 10) * 80) {
			System.out.println("Starvation!");
			die();
		}
		fitness += body.size() * body.size();

		final Direction dir = direction;
		Point2D headNew = dir.apply(body.getFirst());
		lastDirection = dir;

		if (body.contains(headNew)) {
			die();
		} else if (headNew.x < 0 || headNew.y < 0 || headNew.x >= world.width || headNew.y >= world.height) {
			die();
		}

		body.addFirst(headNew);
		if (extraLength > 0) {
			extraLength -= 1;
		} else {
			body.removeLast();
		}

	}

	public void render(Graphics2D g2, int scale) {
		for (Point2D square : body) {
			g2.fillRect(square.x * scale + 1, square.y * scale + 1, scale - 2, scale - 2);
		}
	}

	@Override
	public int compareTo(Snake other) {
		if (this.fitness > other.fitness)
			return -1;
		if (this.fitness < other.fitness)
			return 1;
		return 0;
	}

}
