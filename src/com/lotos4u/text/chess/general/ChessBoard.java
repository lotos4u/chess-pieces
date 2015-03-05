package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Point;

public class ChessBoard {
	
	private int xSize;
	
	private int ySize;
	
	private List<Piece> pieces = new ArrayList<Piece>();

	public ChessBoard(int xSize, int ySize) {
		super();
		this.xSize = xSize;
		this.ySize = ySize;
	}

	public ChessBoard(int xSize, int ySize, List<Piece> pieces) {
		this(xSize, ySize);
		addPieces(pieces);
	}
	
	public void addPieces(List<Piece> pieces){
		this.pieces.addAll(pieces);
		for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
			Piece piece = (Piece) iterator.next();
			piece.setBoard(this);
		}
	}
	
	public List<Piece> arrange(){
		for (Iterator<Piece> pieceIter = pieces.iterator(); pieceIter.hasNext();) {
			Piece piece = (Piece) pieceIter.next();
			if(!piece.isPositioned()){
				List<Point> points = toPoints(); //all board points
				
				for (Iterator<Point> pointIter = points.iterator(); pointIter.hasNext();) {
					Point point = (Point) pointIter.next(); //Current point
					boolean isFree = true; //Current point is free (or not)
					for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
						Piece pc = (Piece) iterator.next(); //Current piece
						if(pc.isPositioned()) //Looking only for positioned pieces
						{
							isFree = isFree && !pc.getPosition().equals(point); //'point' is Not free, if it is a current piece position
							List<Point> takeble = pc.getTakePoints();
							for (Iterator<Point> iter = takeble.iterator(); iter.hasNext();) {
								Point pt = (Point) iter.next();
								isFree = isFree && !pt.equals(point);
							}
						}
					}
					if(isFree)
					{
						piece.setPosition(point);
					}
				}
			}
			//
			
		}

		
		
		return Collections.unmodifiableList(pieces);
	}
	
	public boolean isPointOnBoard(Point point){
		return 
				(point.getX() >= 1) && (point.getX() <= xSize) &&
				(point.getY() >= 1) && (point.getY() <= ySize);
	}
	
	public int getxSize() {
		return xSize;
	}

	public int getySize() {
		return ySize;
	}

	public List<Point> toPoints(){
		List<Point> res = new ArrayList<Point>();
		for (int x = 1; x <= xSize; x++) {
			for (int y = 1; y <= ySize; y++) {
				res.add(new Point(x, y));
			}
		}
		return res;
	}
	
	public boolean isPointTakeble(Point point){
		for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
			Piece piece = (Piece) iterator.next();
			List<Point> points = piece.getTakePoints();
			for (Iterator<Point> iterator2 = points.iterator(); iterator2.hasNext();) {
				Point p = (Point) iterator2.next();
				if(point.equals(p))return true;
			}
		}
		return false;
	}
	
	public Piece getPieceAtPoint(Point point){
		if(isPointOnBoard(point)){
			for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
				Piece piece = (Piece) iterator.next();
				if(piece.getPosition().equals(point))
					return piece;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		List<Point> points = toPoints();
		String res = "";
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			Piece piece = getPieceAtPoint(point);
			res += point + ": " + piece + "\n";
		}
		return res;
	}
	
	
}
