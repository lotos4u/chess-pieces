package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lotos4u.tests.chess.general.Log;
import com.lotos4u.tests.chess.pieces.Bishop;
import com.lotos4u.tests.chess.pieces.King;
import com.lotos4u.tests.chess.pieces.Knight;
import com.lotos4u.tests.chess.pieces.Piece;
import com.lotos4u.tests.chess.pieces.Queen;
import com.lotos4u.tests.chess.pieces.Rook;

public class ChessBoard {
	private Set<ChessBoard> variants = new HashSet<ChessBoard>();
	/**
	 * Recursion counter
	 */
	private int rc = 0;
	private int callCounter = 0;
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
	
	protected List<Point> pointsFree = new ArrayList<Point>();
	protected List<Point> pointsOccupied = new ArrayList<Point>();
	protected List<Point> pointsTakeble = new ArrayList<Point>();

	public ChessBoard (ChessBoard board){
	    this(board.getxSize(), board.getySize());
	    for (Piece p : board.pieces) {
	        Piece newPiece = null;
	        if (p instanceof King) {
                newPiece = new King();
            }else if (p instanceof Knight) {
                newPiece = new Knight();
            }else if (p instanceof Rook) {
                newPiece = new Rook();
            }else if (p instanceof Bishop) {
                newPiece = new Bishop();
            }else if (p instanceof Queen) {
                newPiece = new Queen();
            }
	        Point point = p.getPosition();
	        if(point != null)
	        	newPiece.setPosition(point.getX(), point.getY(), this);
	        pieces.add(newPiece);
		}
	}
	
	public ChessBoard(int newX, int newY) {
		super();
		this.xSize = newX;
		this.ySize = newY;
		for (int x = 1; x <= newX; x++) {
            for (int y = 1; y <= newY; y++) {
                Point point = new Point(x, y);
                points.add(point);
            }
        }
	}

	public Piece getPiece(int index){
	    return pieces.get(index);
	}
	
	public Point getPoint(int index){
	    return points.get(index);
	}
	
	public void addPiece(Piece p) {
		pieces.add(p);
	}
	
	/**
	 * Remove all pieces arrangement by dropping each of them (set null position)
	 */
	public void dropPieces(){
		for (Piece piece : pieces) {
			piece.drop();
		}
	}
	
	protected List<Piece> getWithoutFist(List<Piece> input){
		List<Piece> res = new ArrayList<Piece>(input);
		res.remove(0);
		return res;
	}
		
	
	public boolean tryToPut(Piece piece, Point p) {
		boolean res = false;
		piece.setPosition(p, this);
		res = isArrangeValid();
		if (!res) {
			//Log.out("[" + rc + "] Not Putted " + piece);
			piece.drop();
		} else {
			//Log.out("[" + rc + "] Putted " + piece);
		}
		return res;
	}
	
	public boolean arrangeRecursively(){
		return arrangeRecursively(pieces);
	}
	
	public boolean arrangeRecursively(List<Piece> unpositioned){
		callCounter++;
		rc++;
		boolean res = false;
		boolean putted = false;
		boolean log = true;
		if (log) Log.out("N = " + variants.size() + ", counter = " + callCounter);
		if (unpositioned.size() < 1) {
			rc--;
			return true;
		}
		Piece piece = unpositioned.get(0);
		List<Point> free = getPointsFree();
		if (free.size() < 1) {
			//Log.out("[" + rc + "] No free points!");
			rc--;
			return false;
		}
		//Log. out("[" + rc + "] We have " + free.size() + " points for " + piece);
		
		for (Point point : free) {
			res = false;
			//Log. out("[" + rc + "] Try " + piece + " at " + point);
			putted = tryToPut(piece, point);
			if (putted) {
				res = arrangeRecursively(getWithoutFist(unpositioned));
				if (res) {
					if (isArrangedAndValid()) {
						variants.add(new ChessBoard(this));
					}
				} else {
					
				}
			}
			piece.drop();
		}
		
		rc--;
		return res;		
	}
	public int arrangeRecursivelyVariants(){
		arrangeRecursively();
		Log.out("Number of variants = " + variants.size());
		return variants.size();
	}
	
	public Set<ChessBoard> getVariants(){
		return variants;
	}
	

	
	/**
	 * Test, weather the point located on this board.
	 * @param point - point to test for valid location on this board
	 * @return - true, if point located on the board
	 */
	public boolean isPointOnBoard(Point point){
		return isPointOnBoard(point.getX(), point.getY());
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
	
	public int getPointsNumber(){
	    return xSize*ySize;
	}

	public int getPiecesNumber(){
        return pieces.size();
    }
	public List<Piece> getPiecesUnPositioned(){
		List<Piece> res = new ArrayList<Piece>();
		for (Piece p : pieces) {
			if (!p.isPositioned())
				res.add(p);
		}
		return res;
	}
	/**
	 * 
	 * @return list with point on this board, which are takeble due to positioned pieces
	 */
	public List<Point> getPointsTakeble(){
		Set<Point> res = new HashSet<Point>();
        for (Piece piece : pieces) {
            if (piece.isPositioned()) {
            	res.addAll(piece.getPointsTakeble());
            }
        }
		return new ArrayList<Point>(res);
	}
	
	public boolean isPointFree(Point p) {
		int x = p.getX();
		int y = p.getY();
		for (Piece piece : pieces) {
			Point point = piece.getPosition();
			if (point != null) {
				if ((point.getX() == x) && (point.getY() == y))
					return false;
			} 
		}
		return true;
	}
	/**
	 * 
	 * @return points, which are without pieces and not takeble
	 */
	public List<Point> getPointsFree() {
		//updatePointsLists();
		//return pointsFree;
		
		//List<Point> res = new ArrayList<Point>(points);
		List<Point> res = new ArrayList<Point>();
		List<Point> takeble = getPointsTakeble();
		for(Point p : points) {
            if(isPointFree(p) && !takeble.contains(p))
			//if(!pointsOccupied.contains(p) && !takeble.contains(p))
                res.add(p);
        }
		return res;
		/**/
	}

	
    private void updatePointsLists() {
    	pointsFree.clear();
    	pointsTakeble.clear();
    	pointsOccupied.clear();
    	pointsFree.addAll(points);
    	for (Piece piece : pieces) {
    		Point location = piece.getPosition();
    		if (location != null) {
    			pointsOccupied.add(location);
    			pointsTakeble.addAll(piece.getPointsTakeble());
    		}
    	}
    	for (Piece piece : pieces) {
    		Point location = piece.getPosition();
    		if (location != null) {
    			
    		}
    	}
    	pointsFree.removeAll(pointsTakeble);
    	pointsFree.removeAll(pointsOccupied);
    	return;
    }	

	public boolean isPointTakeble(Point point){
	    for(Piece p : pieces) {
            if(p.isTakePoint(this, point))
                return true;
        }
        return false;        
	}
	
	public Piece getPieceAtPoint(int x, int y){
		return getPieceAtPoint(new Point(x, y));
	}
	
	public Piece getPieceAtPoint(Point point){
		if(isPointOnBoard(point)){
			for(Piece p : pieces) {
				if(p.isPositioned() && p.getPosition().equals(point))
					return p;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		String piecesNumber = "No pieces on the board";
		if(pieces.size() > 0)
		    piecesNumber = pieces.size() + " piece(s) on the board";
		String status = "Arrangement is invalid";
		if(isArrangedAndValid())
		    status = "Arrangement is valid";
		String res = piecesNumber + "\n" + status + "\n";
		for(Point point : points) {
			Piece piece = getPieceAtPoint(point);
			String p = piece != null ? piece.toString() : "-";
			res += point + ": " + p + "\n";
		}
		return res;
	}

    
    
    /**
     * @return true, if there is no piece, which can take any other
     */
    public boolean isArrangeValid(){
        for(Piece p : pieces) {
            if(p.isPositioned() && isPointTakeble(p.getPosition()))
                return false;
        }
        return true;
    }
    /**
     * 
     * @return true, if:
     * 1. All pieces are positioned
     * 2. And there is no piece, which can take any other
     */
    public boolean isArrangedAndValid(){
        if(!isArranged())
            return false;
        return isArrangeValid();
    }

	/**
	 * Check all pieces having not null position.
	 * @return true, if all pieces are positioned
	 */
	public boolean isArranged(){
		for (Piece p : pieces) {
			if(!p.isPositioned())
				return false;
		}
		return true;
	}
	
    private boolean isArrangeEquals(ChessBoard board) {
    	if((pieces.size() != board.pieces.size()) || (points.size() != board.points.size()))
    		return false;
    	for (Point myPoint : points) {
			Piece myPiece = getPieceAtPoint(myPoint);
			Piece hisPiece = board.getPieceAtPoint(myPoint.getX(), myPoint.getY());
			if(!Piece.isPiecesSameKind(myPiece, hisPiece))
				return false;
		}
    	return true;
    }
    	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessBoard b = (ChessBoard) obj;
        if (xSize != b.getxSize())
            return false;
        if (ySize != b.getySize())
            return false;
        if (pieces.size() != b.pieces.size())
            return false;
        if (points.size() != b.points.size())
            return false;
        if (!isArrangeEquals(b))
        	return false;
        return true;        
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + xSize;
        result = prime * result + ySize;
        result = prime * result + pieces.size();
        result = prime * result + points.size();
        return result;
    }
    
    /**
     * Draw a board into System.out
     */
    public void draw() {
    	System.out.print("  ");
    	for (int y = 1; y <= ySize; y++) {
    		System.out.print(" " + y + " ");
    	}
    	System.out.println("");
    	for (int x = 1; x <= xSize; x++) {
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		System.out.print(num);
    		for (int y = 1; y <= ySize; y++) {
    		  	Piece p = getPieceAtPoint(x, y);
    			String name = (p != null) ? p.getShortName() : " ";
    			System.out.print("[" + name + "]");
    		}
    		System.out.println("");
    	}
    	
    }

}
