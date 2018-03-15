package de.markuspi.snake;

public class Point2D {
	
	public final int x;
	public final int y;
	
	public Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Point2D)) return false;
		Point2D other = (Point2D) obj;
		return other.x == this.x && other.y == this.y;			
	}

	@Override
	public int hashCode() {
		return this.x * 17 + this.y;
	}
	
	public String toString() {
		return "(" + x + "|" + y + ")";
	}

}
