package com.lotos4u.text.chess.pieces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lotos4u.text.chess.general.ChessBoard;

abstract public class Piece {

	protected ChessBoard board;
	
	protected Point position = new Point(0, 0);
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
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
		List<Point> takePoints = getTakePoints();
		for (Iterator<Point> iterator = takePoints.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			if(p.equals(point))
				return true;
		}
		return false;
	}
	
	public List<Point> getTakePoints(){
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
		return getName() + " " + position + " [" + getTakePoints() + "]";
	}
	
	public abstract boolean isValidMove(Point point);
	
	public abstract String getName();
	
}

