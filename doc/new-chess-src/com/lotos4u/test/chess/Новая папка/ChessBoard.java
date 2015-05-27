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
	private Set<MicroBoard> variants = new HashSet<MicroBoard>();
	private MicroBoard microBoard;
	private int totalCounter = 0;
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
	
	protected int puttedCounter = 0;
	
    /**
     * All points on the board, from which the board is consists
     */
	protected List<Point> points = new ArrayList<Point>();
	
	private void dropPiece(int pieceIndex) {
		setPosition(pieceIndex, null);
	}
	private void setPosition(int pieceIndex, Point position) {
		Piece p = pieces.get(pieceIndex);
		Point oldPosition = p.getPosition();
		if ((oldPosition == null) && (position != null))
			puttedCounter++;
		if ((oldPosition != null) && (position == null))
			puttedCounter--;
		p.setPosition(position, this);
		
	}
	
	public ChessBoard(int newX, int newY) {
		super();
		xSize = newX;
		ySize = newY;
		microBoard = new MicroBoard(xSize, ySize);
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

	protected List<Piece> getWithoutFist(List<Piece> input){
		List<Piece> res = new ArrayList<Piece>(input);
		res.remove(0);
		return res;
	}
		
	public boolean tryToPut(int pieceIndex, Point p) {
		boolean res = false;
		setPosition(pieceIndex, p);
		res = isArrangeValid();
		if (!res) {
			//Log.out("[" + rc + "] Not Putted " + piece);
			dropPiece(pieceIndex);
		} else {
			//Log.out("[" + rc + "] Putted " + piece);
		}
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
	
	protected boolean isPieceTakesPoint(Piece piece, Point point) {
		Point location = piece.getPosition();
		//if (!isPointOnBoard(point) || (location == null) || location.isSamePoint(point)) 
		if ((location == null) || location.isSamePoint(point))
			return false;
		return piece.canTakePoint(point);
	} 
	
	public boolean cantTouchThis(Piece piece, Point point) {
		Point location = piece.getPosition();
		if ((location == null) || location.isSamePoint(point)) 
			return false;
		return piece.canNotTakePoint(point);
	}
	
	public List<Point> getPointsFree() {
		List<Point> res = new ArrayList<Point>(points);
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			for (Piece piece : pieces) {
				Point location = piece.getPosition();
				if (location != null) {
					if (point.isSamePoint(location) || isPieceTakesPoint(piece, point))
						res.remove(point);
				}
			}
		}
		return res;
	}
	public Point getFirstFreePoint() {
		return getNthFreePoint(0);
	}
	public Point getNthFreePoint(int index) {
		Point res = null;
		List<Point> free = getPointsFree();
		System.out.println("FREE=" + free);
		int size = free.size();
		if (size > 0)
			if(size > index)
				res = free.get(index);
			else
				res = free.get(size-1);
		return res;
	}
	
	public boolean arrangeRecursively(){
		callCounter++;
		rc++;
		boolean res = false;
		boolean putted = false;
		boolean log = false;
		if (true) Log.out("N = " + variants.size() + ", counter = " + callCounter);
		if (isArranged()) {
			rc--;
			return true;
		}
		Piece piece = null;
		int pieceIndex = 0;
		for (Piece p : pieces) {
			if (!p.isPositioned()) {
				piece = p;
				break;
			}
			pieceIndex++;
		}
		
		List<Point> free = getPointsFree();
		//Point point = getFirstFreePoint();
		if (free.size() < 1) {
		//if (point == null) {
			if (log) Log.out("[" + rc + "] No free points!");
			rc--;
			return false;
		}
		if (log) Log. out("[" + rc + "] We have " + free.size() + " points for " + piece);
		//if (log) Log. out("[" + rc + "] We have first free point at " + point + " for " + piece);
		
		int freeCounter = 0;
		for (Point point : free) {
		//while (point != null) {
			res = false;
			if (log) Log. out("[" + rc + "] Try " + piece + " at " + point);
			putted = tryToPut(pieceIndex, point);
			if (putted) {
				res = isArranged() || arrangeRecursively();
				if (res) { //Arrangement successful
					//variants.add(getMicroBoard());
					variants.add(new MicroBoard(this));
					totalCounter++;
				} else { //Arrangement unsuccessful
					
				}
			}
			//point = getFirstFreePoint();
			dropPiece(pieceIndex);
		}
		rc--;
		res = isArranged(); 
		return res;		
	}
	
	public MicroBoard getMicroBoard() {
		microBoard.updateBoard(getArrayView());
		return microBoard;
	}
	public int arrangeRecursivelyVariants(){
		arrangeRecursively();
		Log.out("Number of variants = " + variants.size());
		Log.out("Total counter = " + totalCounter);
		return variants.size();
	}
	
	public Set<MicroBoard> getVariants(){
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
        for (Piece p1 : pieces) {
       		for (Piece p2 : pieces) {
       			if (p1.canTakePiece(p2))
       				return false;
        	}
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
		return puttedCounter == pieces.size();
		/*
		for (Piece p : pieces) {
			if(!p.isPositioned())
				return false;
		}
		return true;
		*/
	}
	
	private boolean isArrayVewEquals(ChessBoard board) {
		char[][] my = getArrayView();
		char[][] his = board.getArrayView();
    	for (int i = 0; i < my.length; i++)
    		for (int j = 0; j < my[i].length; j++)
    			if (my[i][j] != his[i][j])
    				return false;
    	return true;
	}
	
	protected char[][] getArrayView() {
		char[][] res = new char[xSize][ySize];
		for (Piece p : pieces) {
			Point point = p.getPosition();
			if (point != null) {
				int x = point.getX() - 1;
				int y = point.getY() - 1;
				res[x][y] = p.getShortName().charAt(0);
			}
		}
		return res;
	}
	
	private boolean isBoardLikeThis(ChessBoard board) {
		return (pieces.size() == board.getPiecesNumber()) && (xSize == board.getxSize()) && (ySize == board.getySize());
	}
	
    private boolean isArrangeEquals(ChessBoard board) {
    	if(!isBoardLikeThis(board))
    		return false;
    	return isArrayVewEquals(board);
    	/*
    	for (Point myPoint : points) {
			Piece myPiece = getPieceAtPoint(myPoint);
			Piece hisPiece = board.getPieceAtPoint(myPoint.getX(), myPoint.getY());
			if(!Piece.isPiecesSameKind(myPiece, hisPiece))
				return false;
		}
    	return true;
    	*/
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
    	/*
    	char[][] arr = getArrayView();
    	for (int i = 0; i < arr.length; i++)
    		for (int j = 0; j < arr[i].length; j++)
    			System.out.println("Array[" + i + "][" + j + "] = " + arr[i][j]);
    	*/
    }

}
