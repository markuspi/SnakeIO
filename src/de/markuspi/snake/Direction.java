package de.markuspi.snake;

public enum Direction {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

	public int dx, dy;

	private Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;

	}

	public Point2D apply(Point2D pt) {
		return new Point2D(pt.x + dx, pt.y + dy);
	}

	public boolean isOpposite(Direction dir) {
		return (this.dx + dir.dx) == 0 && (this.dy + dir.dy) == 0;
	}

}
