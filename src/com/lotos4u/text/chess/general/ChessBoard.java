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

	public ChessBoard (ChessBoard board){
	    this(board.getxSize(), board.getySize(), board.getPieces());
	    dropPieces();
	}
	
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
		setPieces(pieces);
	}
	/**
	 * Add chess pieces (instead of existed, if they are). 
	 * @param pieces
	 */
	public void setPieces(List<Piece> pieces){
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
			((Piece) iterator.next()).drop();
		}
	}
	
    public List<List<Piece>> arrangePiecesStupidly(){
        List<List<Piece>> res = new ArrayList<List<Piece>>();
        //Initial pieces arrangement
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            piece.setPosition(points.get(i));
        }
        Log.out("Initial arrangement: \n" + this.toString());
        Log.out("Free: " + getPointsFree());
        Log.out("Takeble: " + getPointsTakeble());
        
        dropPieces();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            for (int j = 0; j < pieces.size(); j++) {
                Piece piece = pieces.get(j);
                piece.setPosition(point);
                
            }
        }
        return res;
    }

    public static List<ChessBoard> arrangePiecesVariants(ChessBoard board){
        int nVariants = board.getxSize()*board.getySize();
        ChessBoard[] boards = new ChessBoard[nVariants];
        List<ChessBoard> res = new ArrayList<ChessBoard>();
        int countValid = 0;
        for (int i = 0; i < boards.length; i++) {
            boards[i] = new ChessBoard(board);
            boards[i].arrangePiecesWisely(i);
            Log.out("--- Variant #" + i + " ---\n" + boards[i]);
            if(boards[i].isArrangedAndValid()){
                res.add(boards[i]);
                countValid++;
            }
        }
        Log.out("There are " + countValid + " valid arrangements.");
        return res;
    }
    
    
    
    /**
     * 
     * @return true, if:
     * 1. All pieces are positioned
     * 2. And there is no piece, which can take any other
     */
    public boolean isArrangeValid(){
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if(piece.isPositioned() && isPointTakeble(piece.getPosition())){
                //Log.out("Reason - " + pieces.get(i).getName());
                return false;}
        }
        return true;
    }

    public boolean isArrangedAndValid(){
        if(!isArranged())
            return false;
        return isArrangeValid();
    }    
    
    public void arrangePiecesWisely(int startPoint){
        dropPieces();
        Collections.sort(pieces);
        boolean log = false;
        int index = startPoint;
        index = (index < 0) ? 0 : index;
        index = (index >= points.size()) ? (points.size() - 1) : index;
        //List<Point> localPoints = Collections.unmodifiableList(points);
        //Collections.sort(points);
        if(log)Log.out("\n--- Arrangment start is " + startPoint + " ---");
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(index);
            if(index < (points.size() - 1)){
                index++;
            }
            else{
                index = 0;
            }
            if(log)Log.out("Try point is: " + point);
            if(!point.isFree() || isPointTakeble(point))continue;
            if(log)Log.out("Free point is: " + point);
            for (int j = 0; j < pieces.size(); j++) {
                Piece piece = pieces.get(j);
                if(!piece.isPositioned()){//Choose 1-st non-positioned Piece
                    //Log.out("Put " + pieces.get(j).getName() + " at " + point + "\n");
                    piece.setPosition(point); //And set it new position
                    if(isArrangeValid()){
                        if(log)Log.out(piece + "\n");
                        break;
                    }
                    else{
                        piece.drop();
                    }
                }
            }
        }
        if(log)Log.out("Is arrangement valid: " + isArrangedAndValid());
        return;        
    }
	public void arrangePiecesWisely(){
		dropPieces();
		Collections.sort(pieces);
		boolean log = false;
		for (int i = 0; i < pieces.size(); i++) {
		    //Log.out(index + " of " + pieces.size());
            Piece piece = pieces.get(i); //Get 1-st piece from list
            if(log)Log.out("Try to put " + piece);
            List<Point> free = getPointsFree(); //Get current list with free points on the board
            Collections.sort(free); //
            if(log)Log.out("Free points: " + free);
            if(free.size() > 1){ //If there is at least 2 free point
                /* We should select a point which occupation leads to 
                 * maximum number of free points
                 */
                int selected = 0;
                int size, maxSize = 0;
                for (int j = 0; j < free.size(); j++) {
                    if(log)Log.out("Try to put at " + free.get(j));
                    piece.setPosition(free.get(j)); //try to put current piece on one of the free points
                    size = getPointsFree().size(); //obtain a number of free points after test positioning
                    piece.drop();
                    if(size > maxSize){ //find the index "j" that leads to maximal "size" 
                        maxSize = size;
                        selected = j;
                    }
                    if(log)Log.out(size + " free points");
                }
                piece.setPosition(free.get(selected));
                if(log)Log.out("Piece positioned: " + piece);
            }
            else if (free.size() == 1){
                piece.setPosition(free.get(0));
            }
            else{
                Log.out("No wise solution! Free points are absent for " + piece.getName());
            }
        }
		return;
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
	
	public List<Piece> getPieces() {
        return Collections.unmodifiableList(pieces);
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
		//Log.out("positioned: " + positioned);
		//Log.out("takeble: " + takeble);
		//Log.out("Free: " + res);
		return res;
	}
	
	public boolean isPointTakeble(Point point){
	    for (int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).isTakePoint(point))
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
		String piecesNumber = "No pieces on the board";
		if(pieces.size() > 0)
		    piecesNumber = pieces.size() + " piece(s) on the board";
		String status = "Arrangement is invalid";
		if(isArrangedAndValid())
		    status = "Arrangement is valid";
		String res = piecesNumber + "\n" + status + "\n";
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
