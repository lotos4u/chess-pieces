package com.lotos4u.text.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.text.chess.boards.ChessBoard;
import com.lotos4u.text.chess.boards.Point;

abstract public class Piece implements Comparable<Piece> {

    protected Point position;

    public Point getPosition() {
        return position;
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

    @Override
    public String toString() {
        String pos = "(-,-)";
        if(position != null)
        pos = position.toString();
        return getName() + " " + pos;
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
    
    public abstract List<Point> getPointsTakeble(ChessBoard board);
    
    public abstract boolean isValidMove(Point point);

    public abstract String getName();
    
    public abstract int getTakebility();
}
