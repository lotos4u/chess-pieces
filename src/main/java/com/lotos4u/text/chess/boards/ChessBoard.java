package main.java.com.lotos4u.text.chess.boards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import main.java.com.lotos4u.text.chess.general.Log;
import main.java.com.lotos4u.text.chess.general.Utility;
import main.java.com.lotos4u.text.chess.pieces.Elephant;
import main.java.com.lotos4u.text.chess.pieces.King;
import main.java.com.lotos4u.text.chess.pieces.Knight;
import main.java.com.lotos4u.text.chess.pieces.Piece;
import main.java.com.lotos4u.text.chess.pieces.Queen;
import main.java.com.lotos4u.text.chess.pieces.Rook;

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
	    this(board.getxSize(), board.getySize());
	    for (int i = 0; i < board.getPieces().size(); i++) {
	        Piece p = board.getPiece(i);
	        if (p instanceof King) {
                pieces.add(new King());
            }else if (p instanceof Knight) {
                pieces.add(new Knight());
            }else if (p instanceof Rook) {
                pieces.add(new Rook());
            }else if (p instanceof Elephant) {
                pieces.add(new Elephant());
            }else if (p instanceof Queen) {
                pieces.add(new Queen());
            }
        }
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
	
	public Piece getPiece(int index){
	    return pieces.get(index);
	}
	
	public Point getPoint(int index){
	    return points.get(index);
	}
	
	/**
	 * Get points for specified coordinates
	 * @param x horizontal coordinate
	 * @param y vertical coordinate
	 * @return Point with coordinates (x, y) or null (for invalid coordinates)
	 */
	public Point getPoint(int x, int y){
	    for (Iterator<Point> i = points.iterator(); i.hasNext();) {
            Point point = (Point) i.next();
            if((point.getX() == x) && (point.getY() == y))
                return point;
        }
	    return null;
	}
	
	/**
	 * Remove all pieces arrangement by dropping each of them (set null position)
	 */
	public void dropPieces(){
		for (Iterator<Piece> iterator = pieces.iterator(); iterator.hasNext();) {
			((Piece) iterator.next()).drop();
		}
	}
	@Deprecated
	public List<ChessBoard> arrangePointsForPiecesStupid(int startPoint){
	    List<ChessBoard> res = new ArrayList<ChessBoard>();
	    int pointIndex = Utility.putValueInRange(startPoint, 0, points.size() - 1);
	    for (int i = 0; i < points.size(); i++) {

	        ChessBoard board = new ChessBoard(this);
	        
	        pointIndex = Utility.cycledInc(pointIndex, 0, points.size() - 1);
	        Point point = board.getPoint(pointIndex);
            if(!point.isFree() || isPointTakeble(point))continue;
            
            for (int j = 0; j < pieces.size(); j++) {
                Piece piece = board.getPiece(j);
                if(piece.isPositioned())continue;
                piece.setPosition(point);
                if(!isArrangeValid()){
                    piece.drop();
                }else{
                    break;
                }
            }
            if(board.isArrangedAndValid()){
                res.add(board);
            }
        }
	    return res;
	}
	@Deprecated
    public static List<ChessBoard> arrangePiecesVariantsStupid(ChessBoard inputBoard){
        List<ChessBoard> res = new ArrayList<ChessBoard>();
        int nVariants = inputBoard.getxSize()*inputBoard.getySize();
        int counter = 0;
        while(counter < nVariants){
            ChessBoard board = new ChessBoard(inputBoard);
            board.arrangePointsForPiecesStupid(counter);
            if(board.isArrangedAndValid()){
                Log.out("Board is Valid");
                res.add(board);
            }
            else{
                Log.out("Board is INvalid");
            }
            counter++;
        }
        return res;
    }

    public List<ChessBoard> arrangePiecesVariants(){
        int pointsVariants = getxSize()*getySize();
        int piecesVariants = pieces.size();
        int nVariants = pointsVariants*piecesVariants;
        ChessBoard[] boards = new ChessBoard[nVariants];
        List<ChessBoard> res = new ArrayList<ChessBoard>();
        int countValid = 0;
        int count = 0;
        for (int iPoints = 0; iPoints < pointsVariants; iPoints++) {
            for (int iPieces = 0; iPieces < piecesVariants; iPieces++) {
                Log.out("--- Variant #" + count + ": point #" + iPoints + " " + points.get(iPoints) + ", piece #" + iPieces  + " (" +  pieces.get(iPieces).getName() + ")" + " ---");
                boards[count] = new ChessBoard(this);
                //boards[count].arrangePiecesWisely(iPoints, iPieces);
                boards[count].arrangePiecesOnPoints(iPoints, iPieces);
                Log.out(boards[count]);
                if(boards[count].isArrangedAndValid()){
                    res.add(boards[count]);
                    countValid++;
                }
                count++;
            }//System.exit(0);
        }
        Log.out("There are " + countValid + " valid arrangements.");
        return res;
    }
    
    public void arrangePiecesOnPoints(int startPoint, int startPiece){
        dropPieces();
        boolean log = false;
        int pieceIndex = Utility.putValueInRange(startPiece, 0, pieces.size() - 1);
        int pieceCounter = 0;
        while(pieceCounter < pieces.size()){
            Piece piece = pieces.get(pieceIndex);
            if(log)Log.out("Try to put " + piece.getName());
            pieceIndex = Utility.cycledInc(pieceIndex, 0, pieces.size() - 1);
            boolean positioned = false;
            int pointIndex = Utility.putValueInRange(startPoint, 0, points.size() - 1);
            int pointCounter = 0;
            while (!positioned && pointCounter < points.size()) {
                Point point = points.get(pointIndex); //Select point with index 'pointIndex'
                pointIndex = Utility.cycledInc(pointIndex, 0, points.size() - 1);
                pointCounter++;
                if(!point.isFree() || isPointTakeble(point))continue; //If selected point is free and not takeble
                piece.setPosition(point);
                if(!isArrangeValid()){
                    piece.drop();
                }
                positioned = piece.isPositioned();
                
            }
            if(!positioned){
                if(log)Log.out("INVALID: no point for " + piece.getName());
            }
            else{
                if(log)Log.out(piece.getName() + " putted at " + piece.getPosition());
            }
               
            pieceCounter++;
        }
    }
    
    public void arrangePointsForPieces(int startPoint, int startPiece){
        dropPieces();
        boolean log = false;
        int pointIndex = Utility.putValueInRange(startPoint, 0, points.size() - 1); 
        //if(log)Log.out("\n--- Arrangement start is " + startPoint + " ---");
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(pointIndex); //Select point with index 'pointIndex'
            pointIndex = Utility.cycledInc(pointIndex, 0, points.size() - 1);
            //if(log)Log.out("Try point is: " + point);
            if(!point.isFree() || isPointTakeble(point))continue; //If selected point is free and not takeble
            if(log)Log.out("Free point is: " + point);
            
            //We will try to put at this point next not-positioned piece
            int pieceIndex = Utility.putValueInRange(startPiece, 0, pieces.size() - 1);
            boolean valid = false;
            int counter = 0;
            while(!valid && counter < pieces.size()){
                Piece piece = pieces.get(pieceIndex);
                pieceIndex = Utility.cycledInc(pieceIndex, 0, pieces.size() - 1);
                //Log.out("Try to put " + piece.getName() + " at " + point);
                if(piece.isPositioned()){
                    continue;
                }
                System.out.print("Try " + piece.getName() + " at " + point);
                piece.setPosition(point);
                valid = isArrangeValid();
                if(!valid){
                    piece.drop();
                }
                else{
                   Log.out("\nPutted " + piece.getName() + " at " + point);
                }
                
                counter++;
            }
            
           // if(log)
                if(!valid)Log.out("\nINVALID: no piece for point " + point);
            
        }
        //if(log)
        Log.out("Is arranged: " + isArranged());    
        Log.out("Is valid: " + isArrangedAndValid());
        return;        
    }
    
    
    
    
	public void arrangePiecesOnPoints(){
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

    
    
    /**
     * @return true, if there is no piece, which can take any other
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
