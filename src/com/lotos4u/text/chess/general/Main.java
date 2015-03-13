package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lotos4u.text.chess.pieces.Elephant;
import com.lotos4u.text.chess.pieces.King;
import com.lotos4u.text.chess.pieces.Knight;
import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Queen;
import com.lotos4u.text.chess.pieces.Rook;

public class Main {

    public static void test1(){
        Rook rook1 = new Rook();
        King king1 = new King();
        King king2 = new King();
        ChessBoard board = new ChessBoard(3, 3);
        List<Piece> pieces = new ArrayList<Piece>();
        pieces.add(rook1);
        pieces.add(king1);
        pieces.add(king2);
        board.setPieces(pieces);
        List<ChessBoard> boards = board.arrangePiecesVariants(board);
    }
    
    public static void test2(){
        Rook rook1 = new Rook();
        Rook rook2 = new Rook();
        Knight knight1 = new Knight();
        Knight knight2 = new Knight();
        Knight knight3 = new Knight();
        Knight knight4 = new Knight();
        ChessBoard board = new ChessBoard(3, 3);
        List<Piece> pieces = new ArrayList<Piece>();
        pieces.add(rook1);
        pieces.add(rook2);
        pieces.add(knight1);
        pieces.add(knight2);
        pieces.add(knight3);
        pieces.add(knight4);
        board.setPieces(pieces);
        List<ChessBoard> boards = board.arrangePiecesVariants(board);
    }
    
	public static void main(String[] args) {
	    test1();
	    //test2();
	    
	    System.exit(0);
	    
		Rook rook1 = new Rook();
		Rook rook2 = new Rook();
		Rook rook3 = new Rook();
		Rook rook4 = new Rook();
		
        King king1 = new King();
        King king2 = new King();
        King king3 = new King();
        King king4 = new King();

        Elephant elephant1 = new Elephant();
        Elephant elephant2 = new Elephant();
        Elephant elephant3 = new Elephant();
        Elephant elephant4 = new Elephant();

        Knight knight1 = new Knight();
        Knight knight2 = new Knight();
        Knight knight3 = new Knight();
        Knight knight4 = new Knight();
        
        Queen queen1 = new Queen();
        Queen queen2 = new Queen();
        Queen queen3 = new Queen();
        Queen queen4 = new Queen();

        //Log.out("Compare " + king1.getName() + " to " + rook1.getName() + ": " + king1.compareTo(rook1));
        //Log.out("Compare " + rook1.getName() + " to " + king1.getName() + ": " + rook1.compareTo(king1));
        ChessBoard board33 = new ChessBoard(3, 3);
        ChessBoard board55 = new ChessBoard(5, 5);
		
        ChessBoard board = board33;
		
		//Log.out("Board initial");
		//Log.out(board);
		
		//ChessBoard board2 = new ChessBoard(5, 5);
        //elephant1.setPosition(board2.getPointAt(3, 3));
		//queen1.setPosition(board2.getPointAt(3, 3));
        //Log.out(queen1.getPointsTakeble());
        //System.exit(0);
        
		//Point point = new Point(3, 3);
		//Log.out("Neighbors of " + point + " are: " + point.getNeighbors(board));
		
		//Log.out("Neighbors of " + point + " are: " + point.getNeighbors(board55));
		
		//queen1.setPosition(board.getPointAt(3, 3));
		//Log.out(queen1.getPointsTakeble());System.exit(0);
		List<Piece> pieces = new ArrayList<Piece>();
		//pieces.add(queen1);
		pieces.add(rook1);
		//pieces.add(rook2);
		//pieces.add(rook3);
        pieces.add(king1);
		pieces.add(king2);
        //pieces.add(king3);
		
        //Log.out("Unsorted: " + pieces);
		//Collections.sort(pieces);
		//Log.out("Sorted: " + pieces);
		
		Collections.sort(pieces);
		Log.out("Pieces: " + pieces);
		board.setPieces(pieces);
		
		
		/*
		board.arrangePieces();
		Log.out("Board after normal arrangement");
		Log.out(board);
		Log.out("Free points on the board");
		Log.out(board.getPointsFree());
		*/
		
		//board.arrangePiecesStupidly();
		//System.exit(0);
		
		List<ChessBoard> boards = board.arrangePiecesVariants(board);
		//Log.out("There are " + boards.size() + " valid arrangements:");
		for (int j = 0; j < boards.size(); j++) {
		    //Log.out("--- Variant #" + j + " ---\n" + boards.get(j));    
        }
		
		/*
		board.arrangePiecesWisely(0);
		Log.out("Board after wise arrangement");
		Log.out(board);
        Log.out("Free points on the board");
        Log.out(board.getPointsFree());
        Log.out("Positioned points on the board");
        Log.out(board.getPointsPositioned());
        Log.out("Takeble points on the board");
        Log.out(board.getPointsTakeble());
		 */
	}

	public static void testPoints(){
	    /*
	    Point p1 = new Point(1, 1);
	    Point p2 = new Point(2, 2);
	    Point p3 = new Point(3, 3);
	    Point p4 = new Point(1, 2);
	    
	    ChessBoard board = new ChessBoard(3, 3);
	    p1.setMax(board);
	    p2.setMax(board);
	    p3.setMax(board);
	    p4.setMax(board);
	    
	    List<Point> points = new ArrayList<Point>();
	    points.add(p1);
	    points.add(p2);
	    points.add(p3);
	    points.add(p4);
	    
	    Log.out("Neighbors of " + p1 + " are " + p1.getNeighbors());
	    Log.out("Neighbors of " + p2 + " are " + p2.getNeighbors());
	    Log.out("Neighbors of " + p3 + " are " + p3.getNeighbors());
	    Log.out("Neighbors of " + p4 + " are " + p4.getNeighbors());
	    
	    Log.out("Unsorted points: " + points);
	    Collections.sort(points);
	    Log.out("Sorted points: " + points);
	    
	    */
	}
}
