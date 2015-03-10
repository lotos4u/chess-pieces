package com.lotos4u.text.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.text.chess.general.ChessBoard;


public class Point implements Comparable<Point> {
	/**
	 * Horizontal coordinate of point
	 */
	private int x;
	/**
	 * Vertical coordinate of point
	 */
	private int y;
	/**
	 * Minimal value of horizontal coordinate
	 */
	private int xMin = 1;
    /**
     * Minimal value of vertical coordinate
     */
	private int yMin = 1;
    /**
     * Maximal value of horizontal coordinate
     */
    private int xMax = Integer.MAX_VALUE;
    /**
     * Maximal value of vertical coordinate
     */
    private int yMax = Integer.MAX_VALUE;
	
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

	public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }
    
    public void setMax(ChessBoard board){
        xMax = board.getxSize();
        yMax = board.getySize();
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
		return "(" + x + ", " + y + ") [" + getNeighbors().size() + "]";
	}

	public List<Point> getNeighbors(){
	    List<Point> res = new ArrayList<Point>();
	    if((x + y) < 2)
	        return res;
	    boolean leftable = (x > xMin);
	    boolean uppable = (y > yMin);
	    if(leftable)
	        res.add(new Point(x-1, y));
        if(uppable)
            res.add(new Point(x, y-1));
        if(leftable && uppable)
            res.add(new Point(x-1, y-1));
        
        boolean rightable = (x < xMax);
        boolean downable = (y < yMax);
        if(rightable)
            res.add(new Point(x+1, y));
        if(downable)
            res.add(new Point(x, y+1));
        if(rightable && downable)
            res.add(new Point(x+1, y+1));

        if(leftable && downable)
            res.add(new Point(x-1, y+1));
        if(rightable && uppable)
            res.add(new Point(x+1, y-1));
        
	    return res;
	}
	
	
    @Override
    public int compareTo(Point o) {
        Integer myNeighbors = getNeighbors().size();
        Integer hisNeighbors = o.getNeighbors().size();
        System.out.println(toString() + "(" + myNeighbors + " and " + hisNeighbors + "): " + (myNeighbors.compareTo(hisNeighbors)));
        return myNeighbors.compareTo(hisNeighbors);
    }
	
	
}
