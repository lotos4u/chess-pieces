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
    
    public void setPosition(Point position) {
        if(this.position != null)
            this.position.setPiece(null);
        this.position = position;
        if(position != null)
            position.setPiece(this);
    }

    /**
     * Set position to zeros (make not positioned)
     */
    public void drop() {
        setPosition(null);
    }

    public ChessBoard getBoard() {
        if(position != null)
            return position.getBoard();
        return null;
    }

    public boolean isTakePoint(Point p){
        if(getBoard() == null)
           return false;
        if(!getBoard().equals(p.getBoard()))
            return false;
        List<Point> takePoints = getPointsTakeble();
        return takePoints.contains(p);
    }

    public boolean isPositioned(){
        return (position != null);
    }

    @Override
    public String toString() {
        List<Point> takeble = getPointsTakeble();
        String pos = "(-,-)";
        if(position != null)
            pos = position.toString();
        if(takeble.size() > 0){
            return getName() + " " + pos + ", can take at: [" + takeble + "]";    
        }
        else{
            return getName() + " " + pos;
        }
    }


    public int compareTo(Piece o) {
        Integer t1 = getTakebility();
        Integer t2 = o.getTakebility();
        return t2.compareTo(t1);
    }

    protected List<Point> getBishopTakeble(){
        List<Point> res = new ArrayList<Point>();
        if(position == null)
            return res;
        ChessBoard board = position.getBoard();
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
    
    protected List<Point> getRookTakeble(){
        List<Point> res = new ArrayList<Point>();
        if(position == null)
            return res;
        ChessBoard board = position.getBoard();
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
    
    public abstract List<Point> getPointsTakeble();
    
    public abstract boolean isValidMove(Point point);

    public abstract String getName();
    
    public abstract int getTakebility();
}
