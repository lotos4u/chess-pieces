package com.lotos4u.text.chess.pieces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lotos4u.text.chess.general.ChessBoard;

abstract public class Piece implements Comparable<Piece> {

    protected ChessBoard board;

    protected Point position = new Point(0, 0);

    public Point getPosition() {
        return position;
    }
    
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Set position to zeros (make not positioned)
     */
    public void drop() {
        position.setX(0);
        position.setY(0);
    }
    
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public boolean isTakePoint(Point p){
        if(!board.isPointOnBoard(p) ||!board.isPointOnBoard(position))
            return false;
        List<Point> takePoints = getPointsTakeble();
        for (Iterator<Point> iterator = takePoints.iterator(); iterator.hasNext();) {
            Point point = (Point) iterator.next();
            if(p.equals(point))
                return true;
        }
        return false;
    }

    public List<Point> getPointsTakeble(){
        List<Point> res = new ArrayList<Point>();
        return res;
    }

    public boolean isPositioned(){
        return 
                (board != null) && 
                (board.isPointOnBoard(position)) && 
                (position.getX() > 0) &&
                (position.getY() > 0)
                ;
    }

    @Override
    public String toString() {
        List<Point> takeble = getPointsTakeble();
        if(takeble.size() > 0){
            return getName() + " " + position + ", can take at: [" + takeble + "]";    
        }
        else{
            return getName() + " " + position;
        }
        
    }

    public abstract boolean isValidMove(Point point);

    public abstract String getName();
    
    public abstract int getTakebility();

    @Override
    public int compareTo(Piece o) {
        Integer t1 = getTakebility();
        Integer t2 = o.getTakebility();
        return t2.compareTo(t1);
    }
    
}
