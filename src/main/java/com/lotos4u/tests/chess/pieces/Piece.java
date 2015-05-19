package com.lotos4u.tests.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.tests.chess.boards.ChessBoard;
import com.lotos4u.tests.chess.boards.Point;

abstract public class Piece implements Comparable<Piece> {
	private static int pCounter;
    private String name;
	protected Point position;

    
    
	public Piece(String newName) {
		super();
		name = newName + pCounter++;
	}

	public Point getPosition() {
        return position;
    }

	public void setPosition(int x, int y) {
        position = new Point(x, y);
    }
    
    public void setPosition(Point p) {
        position = p;
    }

	/**
     * Set position to zeros (make not positioned)
     */
    public void drop() {
        setPosition(null);
    }

    public boolean isTakePoint(ChessBoard board, Point p){
        List<Point> takePoints = getPointsTakeble(board);
        return takePoints.contains(p);
    }

    public boolean isPositioned(){
        return (position != null);
    }

    public static boolean isPiecesSameKind(Piece piece1, Piece piece2) {
    	if(piece1 == null) {
    		if(piece2 == null)
    			return true;
    		else
    			return false;
    	}
    	else { //piece1 != null
    		if(piece2 == null)
    			return false;
    		else {
    			return piece1.isSameKind(piece2);
    		}
    	}
    }
    
    @Override
    public String toString() {
        String pos = (position != null) ? (" " + position.toString()) : "";
        return getName() + pos;
    }


    public int compareTo(Piece o) {
        Integer t1 = getTakebility();
        Integer t2 = o.getTakebility();
        return t2.compareTo(t1);
    }

    protected List<Point> getBishopTakeble(ChessBoard board){
        List<Point> res = new ArrayList<Point>();
        if(position == null)
            return res;
        float X = board.getxSize();
        float Y = board.getySize();
        int L = Math.round(Math.min(X,  Y));// (int)Math.round(Math.sqrt(X*X + Y*Y));
        int x = position.getX();
        int y = position.getY();
        Point point;
        for (int i = 1; i <= L; i++) {
            point = board.getPoint(x + i, y + i);
            if(point != null)
                res.add(point);
            
            point = board.getPoint(x - i, y - i);
            if(point != null)
                res.add(point);
            
            point = board.getPoint(x + i, y - i);
            if(point != null)
                res.add(point);
            
            point = board.getPoint(x - i, y + i);
            if(point != null)
                res.add(point);
        }
        return res;
    }
    
    protected List<Point> getRookTakeble(ChessBoard board){
        List<Point> res = new ArrayList<Point>();
        if(position == null)
            return res;
        for (int x = 1; x <= board.getxSize(); x++) {
            if(x != position.getX())
                res.add(board.getPoint(x, position.getY()));
        }
        for (int y = 1; y <= board.getySize(); y++) {
            if(y != position.getY())
                res.add(board.getPoint(position.getX(), y));
        }
        return res;
    }
    
    public String getName() {
		return name;
	}

	public boolean isSameKind(Piece p) {
		return this.getClass().equals(p.getClass());
	}
    
	public abstract List<Point> getPointsTakeble(ChessBoard board);
    
    public abstract int getTakebility();
}
