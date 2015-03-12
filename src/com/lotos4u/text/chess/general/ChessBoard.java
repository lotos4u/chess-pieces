package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Point;

public class ChessBoard {
	/**
	 * Horizontal board size
	 */
	protected int xSize;
	/**
	 * Vertical board size
	 */
	protected int ySize;
	/**
	 * Chess pieces of this board
	 */
	protected List<Piece> pieces = new ArrayList<Piece>();
    /**
     * All points on the board, from which the board is consists
     */
	protected List<Point> points = new ArrayList<Point>();

	public ChessBoard(int xSize, int ySize) {
		super();
		this.xSize = xSize;
		this.ySize = ySize;
		for (int x = 1; x <= xSize; x++) {
            for (int y = 1; y <= ySize; y++) {
                Point point = new Point(x, y, this);
                points.add(point);
            }
        }
	}

	/**
	 * Create a ChessBoard with specified size and chess pieces.
	 * @param xSize - horizontal size
	 * @param ySize - vertical size
	 * @param pieces - list with chess pieces (not positioned)
	 */
	public ChessBoard(int xSize, int ySize, List<Piece> pieces) {
		this(xSize, ySize);
		putPieces(pieces);
	}
	/**
	 * Add chess pieces (instead of existed, if they are). 
	 * @param pieces
	 */
	public void putPieces(List<Piece> pieces){
		this.pieces.clear();
		this.pieces.addAll(pieces);
	}
	
	public Point getPointAt(int x, int y){
	    for (Iterator<Point> i = points.iterator(); i.hasNext();) {
            Point point = (Point) i.next();
            if((point.getX() == x) && (point.getY() == y))
                return point;
        }
	    return null;
	}
	/**
	 * Remove all pieces arrangement by dropping each of them
	 */
	public void dropPieces(){
		for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
			Piece piece = (Piece) iterator.next();
			piece.drop();
		}
	}

	public void arrangePiecesWisely(int initial){
		dropPieces();
		Collections.sort(pieces);
		int index = initial;
		for (int i = 0; i < pieces.size(); i++) {
		    //System.out.println(index + " of " + pieces.size());
            Piece piece = pieces.get(index);
            //System.out.println("Try to put " + piece);
            List<Point> free = getPointsFree();
            Collections.sort(free);
            //System.out.println("Free points: " + free);
            if(free.size() > 0){
                int selected = 0;
                int size, maxSize = 0;
                for (int j = 0; j < free.size(); j++) {
                    //System.out.println("Try to put at " + free.get(j));
                    piece.setPosition(free.get(j));
                    List<Point> all = getPointsAll();
                    size = 0;
                    for (int k = 0; k < all.size(); k++) {
                        if(all.get(k).isFree() && !isPointTakeble(all.get(k)))
                        {
                            size++;
                        }
                    }
                    if(size > maxSize){
                        maxSize = size;
                        selected = j;
                    }
                    //System.out.println(size + " free points");
                }
                //System.out.println("Selected position #" + selected + ": " + free.get(selected));
                piece.setPosition(free.get(selected));
            }
            else{
                //System.out.println("No wise solution! Free points are absent for " + piece.getName());
            }
            if(index < (pieces.size()-1))
                index++;
            else
                index = 0;
        }
		return;
	}
	/**
	 * Arrange chess pieces on the board. Pieces with zero coordinates 
	 * (i.e. not-positioned) are only will arranged.
	 * Positions "takebility" doesn't taking into account. 
	 * @return
	 */
	public List<Piece> arrangePieces(){
		for (Iterator<Piece> pieceIter = pieces.iterator(); pieceIter.hasNext();) {
			Piece piece = (Piece) pieceIter.next();
			if(!piece.isPositioned()){
				List<Point> points = getPointsAll(); //all board points
				for (Iterator<Point> pointIter = points.iterator(); pointIter.hasNext();) {
					Point point = (Point) pointIter.next(); //Current point - one of all possible points on a board
					boolean isFree = true; //Current point is free (or not)
					for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
						Piece pc = (Piece) iterator.next(); //Current piece
						if(pc.isPositioned()) //Looking only for positioned pieces
						{
							isFree = isFree && !pc.getPosition().equals(point); //'point' is Not free, if it is a current piece position
							List<Point> takeble = pc.getPointsTakeble();
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
	
	public boolean isArranged(){
		for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
			Piece piece = (Piece) iterator.next();
			if(!piece.isPositioned())
				return false;
		}
		return true;
	}
	/**
	 * Test, weather the point located on this board.
	 * @param point - point to test for valid location on this board
	 * @return - true, if point located on the board
	 */
	public boolean isPointOnBoard(Point point){
		return 
				(point.getX() >= 1) && (point.getX() <= xSize) &&
				(point.getY() >= 1) && (point.getY() <= ySize);
	}
	/**
	 * Test, weather the point located on this board.
	 * @param x - horizontal point coordinate
	 * @param y - vertical point coordinate
	 * @return - true, if point located on the board
	 */
    public boolean isPointOnBoard(int x, int y){
        return 
                (x >= 1) && (x <= xSize) &&
                (y >= 1) && (y <= ySize);
    }
	
	public int getxSize() {
		return xSize;
	}

	public int getySize() {
		return ySize;
	}
	/**
	 * @return list with all points, located on this board
	 */
	public List<Point> getPointsAll(){
		return Collections.unmodifiableList(points);
	}
	/**
	 * 
	 * @return list with point on this board, which are takeble due to positioned pieces
	 */
	public List<Point> getPointsTakeble(){
		List<Point> res = new ArrayList<Point>();
        for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
            Piece piece = (Piece) iterator.next();
            if(piece.isPositioned()){
                List<Point> takeble = piece.getPointsTakeble();
                for (int i = 0; i < takeble.size(); i++) {
                    if(!res.contains(takeble.get(i)))res.add(takeble.get(i));
                }
            }
        }
		return res;
	}
	/**
	 * 
	 * @return list with board points on what peaces are already positioned
	 */
	public List<Point> getPointsPositioned() {
		List<Point> res = new ArrayList<Point>();
		for (Iterator<Point> i = points.iterator(); i.hasNext();) {
            Point p = (Point) i.next();
            if(p.getPiece() != null)
                res.add(p);
        }
		return res;
	}
	/**
	 * 
	 * @return points, which are without pieces and not takeble
	 */
	public List<Point> getPointsFree() {
		List<Point> res = new ArrayList<Point>();
		List<Point> takeble = getPointsTakeble();
		for (int i = 0; i < points.size(); i++) {
            if(points.get(i).isFree() && !takeble.contains(points.get(i)))
                res.add(points.get(i));
        }
		//System.out.println("positioned: " + positioned);
		//System.out.println("takeble: " + takeble);
		//System.out.println("Free: " + res);
		return res;
	}
	
	public boolean isPointTakeble(Point point){
        for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
            Piece piece = (Piece) iterator.next();
            if(piece.isTakePoint(point))
                return true;
        }
        return false;        
	}
	
	public Piece getPieceAtPoint(Point point){
		if(isPointOnBoard(point)){
			for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
				Piece piece = (Piece) iterator.next();
				if(piece.isPositioned() && piece.getPosition().equals(point))
					return piece;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		List<Point> points = getPointsAll();
		String res = "";
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			Piece piece = getPieceAtPoint(point);
			String p = piece != null ? piece.toString() : "-";
			res += point + ": " + p + "\n";
		}
		return res;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessBoard c = (ChessBoard) obj;
        if (xSize != c.getxSize())
            return false;
        if (ySize != c.getySize())
            return false;
        if (!pieces.equals(obj))
            return false;
        if (!points.equals(obj))
            return false;
        return true;        
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + xSize;
        result = prime * result + ySize;
        result = prime * result + pieces.hashCode();
        result = prime * result + points.hashCode();
        return result;
    }
}
