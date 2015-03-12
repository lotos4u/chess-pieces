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

	public static void main(String[] args) {
	    //testPoints();System.exit(0);
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

        //System.out.println("Compare " + king1.getName() + " to " + rook1.getName() + ": " + king1.compareTo(rook1));
        //System.out.println("Compare " + rook1.getName() + " to " + king1.getName() + ": " + rook1.compareTo(king1));
        WiseChessBoard board33 = new WiseChessBoard(3, 3);
		WiseChessBoard board55 = new WiseChessBoard(5, 5);
		
		WiseChessBoard board = board33;
		
		System.out.println("Board initial");
		System.out.println(board);
		
		//ChessBoard board2 = new ChessBoard(5, 5);
        //elephant1.setPosition(board2.getPointAt(3, 3));
		//queen1.setPosition(board2.getPointAt(3, 3));
        //System.out.println(queen1.getPointsTakeble());
        //System.exit(0);
        
		//Point point = new Point(3, 3);
		//System.out.println("Neighbors of " + point + " are: " + point.getNeighbors(board));
		
		//System.out.println("Neighbors of " + point + " are: " + point.getNeighbors(board55));
		
		//queen1.setPosition(board.getPointAt(3, 3));
		//System.out.println(queen1.getPointsTakeble());System.exit(0);
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(queen1);
		pieces.add(rook1);
		pieces.add(rook2);
		//pieces.add(king1);
		//pieces.add(rook3);
        //pieces.add(king2);
        //pieces.add(king3);
		
        //System.out.println("Unsorted: " + pieces);
		//Collections.sort(pieces);
		//System.out.println("Sorted: " + pieces);
		
		Collections.sort(pieces);
		System.out.println("Pieces: " + pieces);
		board.putPieces(pieces);
		
		
		/*
		board.arrangePieces();
		System.out.println("Board after normal arrangement");
		System.out.println(board);
		System.out.println("Free points on the board");
		System.out.println(board.getPointsFree());
		*/
		
		//board.arrangePiecesStupidly();
		//System.exit(0);
		
		board.arrangePiecesWisely(0);
		System.out.println("Board after wise arrangement");
		System.out.println(board);
        System.out.println("Free points on the board");
        System.out.println(board.getPointsFree());
        System.out.println("Positioned points on the board");
        System.out.println(board.getPointsPositioned());
        System.out.println("Takeble points on the board");
        System.out.println(board.getPointsTakeble());

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
	    
	    System.out.println("Neighbors of " + p1 + " are " + p1.getNeighbors());
	    System.out.println("Neighbors of " + p2 + " are " + p2.getNeighbors());
	    System.out.println("Neighbors of " + p3 + " are " + p3.getNeighbors());
	    System.out.println("Neighbors of " + p4 + " are " + p4.getNeighbors());
	    
	    System.out.println("Unsorted points: " + points);
	    Collections.sort(points);
	    System.out.println("Sorted points: " + points);
	    
	    */
	}
}
