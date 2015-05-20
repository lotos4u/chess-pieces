package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.lotos4u.tests.chess.general.Log;
import com.lotos4u.tests.chess.general.Utility;
import com.lotos4u.tests.chess.pieces.Bishop;
import com.lotos4u.tests.chess.pieces.King;
import com.lotos4u.tests.chess.pieces.Knight;
import com.lotos4u.tests.chess.pieces.Piece;
import com.lotos4u.tests.chess.pieces.Queen;
import com.lotos4u.tests.chess.pieces.Rook;

public class ChessBoard {
	/**
	 * Recursion counter
	 */
	private int rc = 0;
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
	        	newPiece.setPosition(point.getX(), point.getY());
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
		for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
			iterator.next().drop();
		}
	}
	
	protected List<Piece> getUnpositioned(List<Piece> input){
		List<Piece> res = new ArrayList<Piece>();
		for (Piece piece : input) {
			if (!piece.isPositioned())res.add(piece);
		}
		return res;
	}	
	
	public boolean arrangeRecursively(int startPoint, int startPiece){
	    return arrangeRecursively(startPoint, startPiece, pieces);
	}

	protected boolean arrangeRecursively(int startPoint, int startPiece, List<Piece> unpositioned){
		boolean log = false;
		boolean log_local = false;
		boolean pauses = false;
		boolean retVal = false;
		boolean recursionResult = false;
		List<Point> possible = new ArrayList<Point>(points);
        rc++; //Recursion counter
        if(log)Log.out("[" + rc + "] " + "Pieces:" + unpositioned);
        int pieceIndex = startPiece;
        int pieceCounter = 0;
        pieceIndex = Utility.putValueInRange(pieceIndex, 0, unpositioned.size()-1);
        Piece piece = unpositioned.get(pieceIndex);
        Piece prev = null;
        while(!piece.isPositioned() && (pieceCounter++ < unpositioned.size())) {
            if(log)Log.out("[" + rc + "] " + "Try to put " + piece + ", possible:" + possible);
            retVal = tryToPut(piece, possible, startPoint, log_local);
            if(pauses)Log.pause();
            if (retVal) {
            	if(log)Log.out("[" + rc + "] " + "Success! " + piece);
            	if(unpositioned.size() > 1) {
            		recursionResult = arrangeRecursively(startPoint, startPiece, getUnpositioned(unpositioned));
                    if(log)Log.out("[" + rc + "] " + "Recursive call returned..." + recursionResult);
                    if(!recursionResult) {
                    	if(log)Log.out("[" + rc + "] " + "Drop last piece..." + piece);
                    	possible.remove(piece.getPosition());
                    	piece.drop();
                    }
            	}
            	else {
                    if(log)Log.out("[" + rc + "] " + "Arrangment complete!\n");
            	}                	
            }
            else
            {
        		if(log)Log.out("[" + rc + "] " + "Fail with " + piece + "...");
        		prev = piece;
        		int selectCounter = 0;
        		while(!piece.isSameKind(prev) && (selectCounter < unpositioned.size())) {
        			pieceIndex = Utility.cycledInc(pieceIndex, 0, unpositioned.size()-1);
        			piece = unpositioned.get(pieceIndex);
        		}
        		if(piece.isSameKind(prev))pieceCounter =  unpositioned.size();
        		if(log)Log.out("[" + rc + "] " + "Nothing to choose more... exiting...");
            }
        }
        if(!piece.isPositioned()){
        	if(log)Log.out("[" + rc + "] " + "Arrangement is impossible: start point=" + startPoint + ", start piece=" + startPiece + " (Pieces: " + pieces + "\n");
        }
        rc--;
        return retVal;
	}

	protected boolean tryToPut(Piece piece, List<Point> possible, int startIndex, boolean log) {
		int i = 0;
		boolean pauses = false;
		int pointIndex = Utility.putValueInRange(startIndex, 0, possible.size()-1);
		List<Point> pts = new ArrayList<Point>(possible); 
        while((pts.size() > 0) && !piece.isPositioned() && (i++ < pts.size())) {
            Point point = pts.get(pointIndex);
            boolean isFree = isPointFree(point);
            boolean isTakeble = isPointTakeble(point);
            if(isFree && !isTakeble){
            	if(log)Log.out("[" + rc + "] " + "try point index=" + pointIndex + ", " + point);
                piece.setPosition(point);
                if(isArrangeValid()){
                    if(log)Log.out("[" + rc + "] " + "Piece positioned correctly: " + piece);
                }
                else{
                	if(log)Log.out("[" + rc + "] " + "Invalid arrangement! Have to drop...");
                    piece.drop();
                }
            }
            pointIndex = Utility.cycledInc(pointIndex, 0, possible.size()-1);
            if(pauses)Log.pause();
        }
        return (piece.isPositioned());
	}
	
	public int arrangeRecursivelyVariants(){
		long start = System.currentTimeMillis();
		Log.out("Board (" + xSize + ", " + ySize + "), " + pieces);
		boolean log = true;
	    int validCounter = 0; //Counter of all possible valid arrangements
	    int attemptsCounter = 0; 
	    Set<ChessBoard> res = new HashSet<ChessBoard>(); //Set will filter equal arrangements
        for (int startPoint = 0; startPoint < points.size(); startPoint++) {
            for (int startPiece = 0; startPiece < pieces.size(); startPiece++) {
                dropPieces(); //Drop current arrangement
                if(log)Log.out("\n\n*********  Try (point=" + startPoint + " " + points.get(startPoint) + ", piece=" + startPiece + " (" + pieces.get(startPiece) + ")) *********");
                arrangeRecursively(startPoint, startPiece); //Perform arrangement for 'startPoint' and 'startPiece'
                attemptsCounter++;
                if(isArrangedAndValid()){
                	//if(log)Log.out("Arrange Valid, point=" + startPoint + ", piece=" + startPiece + " (Pieces: " + pieces);
                	if(log) {Log.out("Arrange Valid");draw();}
                    validCounter++;
                    res.add(new ChessBoard(this));
                }
                else {
                	//if(log)Log.out("Invalid");
                	if(log) {Log.out("Arrange InValid");draw();}
                }
            }
        }
        if(log)Log.out("Attempts number = " + attemptsCounter);
        if(log)Log.out("Valid arranges number (before Set-filter) = " + validCounter);
        validCounter = res.size();
        Log.out("Valid arranges number (after Set-filter) = " + validCounter);
        long end = System.currentTimeMillis();
        Log.out("Arrangement performed in " + (end - start) + " ms");
        return validCounter;
	}
	

	/**
	 * Check all pieces having not null position.
	 * @return true, if all pieces are positioned
	 */
	public boolean isArranged(){
	    for (int i = 0; i < pieces.size(); i++) {
            if(!pieces.get(i).isPositioned())
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
	/**
	 * 
	 * @return list with point on this board, which are takeble due to positioned pieces
	 */
	public List<Point> getPointsTakeble(){
		Set<Point> res = new HashSet<Point>();
        for (Piece piece : pieces) {
            if(piece.isPositioned()){
                for(Point p : piece.getPointsTakeble(this)) {
                	res.add(p);
                }
            }
        }
		return new ArrayList<Point>(res);
	}
	
	/**
	 * 
	 * @return list with board points on what peaces are already positioned
	 */
	public List<Point> getPointsPositioned() {
		List<Point> res = new ArrayList<Point>();
		for (Iterator<Piece> i = pieces.iterator(); i.hasNext();) {
			Point p = i.next().getPosition();
            if(p != null)
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
		for(Point p : points) {
            if(isPointFree(p) && !takeble.contains(p))
                res.add(p);
        }
		return res;
	}
	
	public boolean isPointFree(Point point){
	    for (int i = 0; i < pieces.size(); i++) {
	    	Point p = pieces.get(i).getPosition();
	    	if((p != null) && p.isPointEquals(point))return false;
        }
        return true;        
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
