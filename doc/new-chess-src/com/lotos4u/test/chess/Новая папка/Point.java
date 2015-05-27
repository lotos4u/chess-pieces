package com.lotos4u.tests.chess.boards;



//public class Point implements Comparable<Point> {
public class Point {
	/**
	 * Horizontal coordinate of point
	 */
	private int x;
	/**
	 * Vertical coordinate of point
	 */
	private int y;


	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isSamePoint(Point p) {
		return (x == p.x) && (y == p.y);
	}

	public boolean isDifferentPoint(Point p) {
		return (x != p.x) || (y != p.y);
	}
	
    @Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point p = (Point) obj;
        if (x != p.getX())
            return false;
        if (y != p.getY())
            return false;
        return true;		
	}

	@Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
	}

	@Override
	public String toString() {
	    return "(" + x + ", " + y + ")";
	}
}
