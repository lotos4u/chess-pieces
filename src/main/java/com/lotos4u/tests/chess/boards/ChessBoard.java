package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lotos4u.tests.chess.general.Log;
import com.lotos4u.tests.chess.pieces.Piece;

public class ChessBoard extends AbstractChessBoard {
	//private Set<MicroBoard> uniqueVariants = new HashSet<MicroBoard>();
	private Set<MicroBoard> uniqueVariants = new HashSet<MicroBoard>();
	private List<MicroBoard> allVariants = new ArrayList<MicroBoard>();
		
	/**
	 * Chess pieces of this board
	 */
	protected List<Piece> pieces = new ArrayList<Piece>();
    /**
     * All points on the board, from which the board is consists
     */
	protected List<Point> points = new ArrayList<Point>();

	private char[] boardView;
	
	protected int putCounter = 0;
	protected int tryToPutCounter = 0;
	private int recursionDepth = 0;
	private int recursiveCallCounter = 0;
	private int allVariantsCounter = 0;
	private int uniqueVariantsCounter = 0;
	
	private long recursionStart = 0;
	private long recursionFinish = 0;
	private long sortStart = 0;
	private long sortFinish = 0;


	private boolean sortAfter = true;
	
	
	public ChessBoard(int newX, int newY) {
		super(newX, newY);
		boardView = new char[xSize*ySize];
		for (int x = 1; x <= newX; x++) {
            for (int y = 1; y <= newY; y++) {
                Point point = new Point(x, y);
                points.add(point);
            }
        }
	}
	
	public int getNPieces() {
		return pieces.size();
	}

	public Piece getPiece(int index){
	    return pieces.get(index);
	}
	
	public Point getPoint(int index){
	    return points.get(index);
	}
	
	public void addPiece(Piece p) {
		pieces.add(p);
		nPieces = pieces.size();
	}

	protected List<Piece> getWithoutFist(List<Piece> input){
		List<Piece> res = new ArrayList<Piece>(input);
		res.remove(0);
		return res;
	}
	private void dropPiece(int pieceIndex) {
		setPosition(pieceIndex, null);
	}
	private void setPosition(int pieceIndex, Point position) {
		Piece p = pieces.get(pieceIndex);
		Point oldPosition = p.getPosition();
		if ((oldPosition == null) && (position != null)) {
			putCounter++;
		}
		if ((oldPosition != null) && (position == null)) {
			putCounter--;
			boardView[getIndexForPoint(oldPosition.getX()-1, oldPosition.getY()-1)] = ' ';
		}
		if (position != null)
			boardView[getIndexForPoint(position.getX()-1, position.getY()-1)] = p.getShortName();
		p.setPosition(position, this);
	}
			
	public boolean tryToPut(int pieceIndex, Point p, boolean log) {
		tryToPutCounter++;
		boolean res = false;
		setPosition(pieceIndex, p);
		res = isArrangeValid();
		if (!res) {
			if(log)Log.out("[" + recursionDepth + "] Not Putted #" + pieceIndex);
			dropPiece(pieceIndex);
		} else {
			if(log)Log.out("[" + recursionDepth + "] Putted #" + pieceIndex);
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
		List<Point> prom = new ArrayList<Point>();
		boolean isWhite;
		for (Point point : points) {
			isWhite = true;
			for (Piece piece : pieces) {
				Point location = piece.getPosition();
				if (location != null) {
					if (location.isSamePoint(point) || isPieceTakesPoint(piece, point)) {
						isWhite = false;
						break;
					}
				}
			}
			if (isWhite)
				prom.add(point);
		}
		return new ArrayList<Point>(prom);
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
		recursiveCallCounter++;
		recursionDepth++;
		boolean res = false;
		boolean putted = false;
		boolean log = false;
		boolean logInner = false;
		boolean logExtra = true;
		
		//if (logExtra) Log.out("N = " + uniqueVariants.size() + ", counter = " + recursiveCallCounter + ", " + getSecondsFromStart() + " sec");
		//if (logExtra)Log.out("Counter = " + recursiveCallCounter + ", " + getSecondsFromStart() + " sec");
		//if (logExtra)Log.out("Counter = " + recursiveCallCounter);
		//if (logExtra)Log.out(getSecondsFromStart() + " sec");
		//if (logExtra)Log.out("N variants = " + allVariantsCounter);
		
		if (isArranged()) {
			recursionDepth--;
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
		if (free.size() < 1) {
			if (log) Log.out("[" + recursionDepth + "] No free points!");
			recursionDepth--;
			return false;
		}
		if (log) Log. out("[" + recursionDepth + "] We have " + free.size() + " points for " + piece);
		if (log) Log. out("[" + recursionDepth + "] Pieces:" + pieces);
		if (log) Log. out("[" + recursionDepth + "] Free:" + free);
		
		for (Point point : free) {
			res = false;
			if (log) Log. out("[" + recursionDepth + "] Try " + piece + " at " + point);
			putted = tryToPut(pieceIndex, point, logInner);
			if (putted) {
				res = isArranged() || arrangeRecursively();
				if (res) { //Arrangement successful
					MicroBoard m = new MicroBoard(this);
					allVariantsCounter++;
					if (sortAfter) {
						allVariants.add(m);
					} else {
						boolean isUnique = uniqueVariants.add(m);	
						if (isUnique)
							uniqueVariantsCounter++;
					}
				} else { //Arrangement unsuccessful
					
				}
			}
			dropPiece(pieceIndex);
		}
		
		recursionDepth--;
		res = isArranged(); 
		return res;		
	}
	
	public int arrangeVariants(boolean logSummary){
		recursionStart = System.currentTimeMillis();
		recursiveCallCounter = 0;
		allVariantsCounter = 0;
		uniqueVariantsCounter = 0;
		putCounter = 0;
		tryToPutCounter = 0;
		allVariants.clear();
		uniqueVariants.clear();
		MicroBoard.equalsCounter = 0;
		MicroBoard.hashCounter = 0;
		Collections.sort(pieces);
		arrangeRecursively();
		recursionFinish = System.currentTimeMillis();
		long recTime = recursionFinish - recursionStart;
		long sortTime = 0;
		if (logSummary) Log.out("Chess complexity is = " + xSize*ySize*(pieces.size()));
		if (sortAfter) {
			sortStart = System.currentTimeMillis();	
			uniqueVariants.addAll(allVariants);
			sortFinish = System.currentTimeMillis();
			sortTime = sortFinish - sortStart;
		}
		int res = uniqueVariants.size();
		if (logSummary) Log.out("Number of all variants = " + allVariantsCounter);
		if (logSummary) Log.out("Number of unique variants = " + res);
		if (sortAfter) {
			if (logSummary) Log.out("Arrangements time = " + recTime + " ms");
			if (logSummary) Log.out("Sort time = " + sortTime + " ms");
		}
		if (logSummary) Log.out("Full time = " + (recTime + sortTime) + " ms");
		if (logSummary) Log.out("Number of recursive calls = " + recursiveCallCounter);
		if (recTime > 0) 
			if (logSummary) Log.out("Number of recursive calls per ms = " + (float)(recursiveCallCounter/recTime));
		if (logSummary) Log.out("Number of put tries = " + tryToPutCounter);
		if (logSummary) Log.out("Number of Equals calls = " + MicroBoard.equalsCounter);
		if (logSummary) Log.out("Number of Hashcode calls = " + MicroBoard.hashCounter);
		return res;
	}
	
	private String getSecondsFromStart() {
		long sec = (System.currentTimeMillis() - recursionStart)/1000;
		//long end = System.currentTimeMillis();
		//Date d1 = new Date(miliStart);
		//Date d2 = new Date(end);
		//Date d3 = new Date(end - miliStart);
		return "" + sec;
	}
	
	public Set<MicroBoard> getVariants(){
		return uniqueVariants;
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
		return putCounter == pieces.size();
		/*
		for (Piece p : pieces) {
			if(!p.isPositioned())
				return false;
		}
		return true;
		*/
	}
	

	public int getIndexForPoint(int x, int y) {
		return y + ySize*x;//=index
	}
	
	private boolean isBoardLikeThis(ChessBoard board) {
		return (pieces.size() == board.getNPieces()) && (xSize == board.getxSize()) && (ySize == board.getySize());
	}
	
    private boolean isArrangeEquals(ChessBoard board) {
    	if(!isBoardLikeThis(board))
    		return false;
    	//return isArrayVewEquals(board);
    	
    	for (Point myPoint : points) {
			Piece myPiece = getPieceAtPoint(myPoint);
			Piece hisPiece = board.getPieceAtPoint(myPoint.getX(), myPoint.getY());
			if(!Piece.isPiecesSameKind(myPiece, hisPiece))
				return false;
		}
    	return true;
    }

    public boolean isDifferentGame(ChessBoard b) {
    	return (b.xSize != xSize) || 
    			(b.ySize != ySize) ||
    			(b.points.size() != points.size());
    }
    /*
    public boolean isArrangeEquals(ChessBoard b) {
    	if (isDifferentGame(b))
    		return false;
    	//return Arrays.equals(boardView, b.boardView);
    	
    	for (Point p : points) {
    		int x = p.getX();
    		int y = p.getY();
   			if (b.boardView[x][y] != boardView[x][y])
   				return false;
    	}
    	return true;
    }  
*/    
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
    			String name = (p != null) ? "" + p.getShortName() : " ";
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
/*    
    public static void main(String arg[]) {
    	ChessBoard board = new ChessBoard(4, 4);
    	board.addPiece(new Rook());
    	board.addPiece(new Rook());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	long start = System.currentTimeMillis();
        int res = board.arrangeVariants();
        long end = System.currentTimeMillis();
        Log.out("Arrangement performed in " + (end - start) + " ms");

        int counter = 0;
        for (MicroBoard b : board.getVariants()) {
        	System.out.println("Variant " + (counter++));
        	System.out.println(b.getBoardViewAsString());;
        }    	
    }
*/

	public char[] getBoardViewAsArray() {
		//return boardView;
		char[] res = new char[xSize*ySize];
		for (Piece p : pieces) {
			Point point = p.getPosition();
			if (point != null) {
				int x = point.getX() - 1;
				int y = point.getY() - 1;
				int pointIndex = getIndexForPoint(x, y);
				res[pointIndex] = p.getShortName();
			}
		}
		return res;
	}
}
