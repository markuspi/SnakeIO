package de.markuspi.snake;

public class Vector2D {

	public double x;
	public double y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public int xInt() {
		return (int) x;
	}

	public int yInt() {
		return (int) y;
	}

	public void add(Vector2D vector) {
		this.x += vector.x;
		this.y += vector.y;
	}

	public void scale(double factor) {
		this.x *= factor;
		this.y *= factor;
	}

	public void rotate(double angle) {
		double xn = x * Math.cos(angle) - y * Math.sin(angle);
		double yn = x * Math.sin(angle) + y * Math.cos(angle);

		x = xn;
		y = yn;
	}

	public void normalize() {
		double len = length();
		x /= len;
		y /= len;
	}

	public double getAngle() {
		return Math.atan2(x, y);
	}

	public double lengthSquared() {
		return x * x + y * y;
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	public String toString() {
		return String.format("<%.2f|%.2f>", x, y);
	}

}
