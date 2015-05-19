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
	
	public boolean isPointEquals(Point p){
		return (p.getX() == x) && (p.getY() == y);
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
/*
	public List<Point> getPossibleNeighbors(){
	    List<Point> res = new ArrayList<Point>();
	    if((x + y) < 2)
	        return res;
	    boolean leftable = (x > 1);
	    boolean uppable = (y > 1);
	    if(leftable)
	        res.add(board.getPoint(x-1, y));
        if(uppable)
            res.add(board.getPoint(x, y-1));
        if(leftable && uppable)
            res.add(board.getPoint(x-1, y-1));
        
        boolean rightable = (x < board.getxSize());
        boolean downable = (y < board.getySize());
        if(rightable)
            res.add(board.getPoint(x+1, y));
        if(downable)
            res.add(board.getPoint(x, y+1));
        if(rightable && downable)
            res.add(board.getPoint(x+1, y+1));

        if(leftable && downable)
            res.add(board.getPoint(x-1, y+1));
        if(rightable && uppable)
            res.add(board.getPoint(x+1, y-1));
        
	    return res;
	}
	*/

/*    
    public int compareTo(Point o) {
        Integer myNeighbors = getNeighbors().size();
        Integer hisNeighbors = o.getNeighbors().size();
        return myNeighbors.compareTo(hisNeighbors);
    }
    */
}
