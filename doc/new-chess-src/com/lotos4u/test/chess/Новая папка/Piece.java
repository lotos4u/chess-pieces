package com.lotos4u.tests.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.tests.chess.boards.ChessBoard;
import com.lotos4u.tests.chess.boards.Point;

abstract public class Piece implements Comparable<Piece> {
	private static int pCounter;
    private String name;
    private String shortName;
	protected Point position;
    
    
	public Piece(String newName) {
		super();
		name = newName + pCounter++;
		shortName = name.substring(0, 1);
		if (this instanceof Knight) {
			shortName = "N";
		}
	}

	public Point getPosition() {
        return position;
    }

	public void setPosition(int x, int y, ChessBoard board) {
        setPosition(new Point(x, y), board);
    }
    
    public void setPosition(Point p, ChessBoard board) {
        position = p;
    }

	/**
     * Set position to zeros (make not positioned)
     */
    public void drop() {
        setPosition(null, null);
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
        if((position == null) || (board == null))
            return res;
        int x = position.getX();
        int y = position.getY();
        for (int i = 1; i <= Math.max(board.getxSize(), board.getySize()); i++) {
        	int newX, newY;
        	newX = x + i;
        	newY = y + i;
        	if (board.isPointOnBoard(newX, newY))
        		res.add(new Point(newX, newY));
        	newX = x - i;
        	newY = y - i;
        	if (board.isPointOnBoard(newX, newY))
        		res.add(new Point(newX, newY));
        	newX = x + i;
        	newY = y - i;
        	if (board.isPointOnBoard(newX, newY))
        		res.add(new Point(newX, newY));
        	newX = x - i;
        	newY = y + i;
        	if (board.isPointOnBoard(newX, newY))
        		res.add(new Point(newX, newY));
        }
        return res;
    }
    
    protected List<Point> getRookTakeble(ChessBoard board){
        List<Point> res = new ArrayList<Point>();
        if((position == null) || (board == null))
            return res;
        int X = position.getX();
        int Y = position.getY();
        for (int x = 1; x <= board.getxSize(); x++) {
            if(x != X)
                res.add(new Point(x, Y));
        }
        for (int y = 1; y <= board.getySize(); y++) {
            if(y != Y)
                res.add(new Point(X, y));
        }
        return res;
    }
    
    public String getName() {
		return name;
	}

    public String getShortName() {
		return shortName;
	}

	public boolean isSameKind(Piece p) {
		return this.getClass().equals(p.getClass());
		/*
		return 
				((this instanceof King) && (p instanceof King)) ||
				((this instanceof Knight) && (p instanceof Knight)) ||
				((this instanceof Bishop) && (p instanceof Bishop)) ||
				((this instanceof Queen) && (p instanceof Queen)) ||
				((this instanceof Rook) && (p instanceof Rook))
				;
			*/
		
	}
	public abstract boolean canTakePoint(Point point);
	
	public boolean canNotTakePoint(Point point) {
		return !canTakePoint(point);
	}
	
	public boolean canTakePiece(Piece piece) {
		return canTakePoint(piece.getPosition());
	}

	public boolean canNotTakePiece(Piece piece) {
		return canNotTakePoint(piece.getPosition());
	}
	
    public abstract int getTakebility();
}
