package main.java.com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.com.lotos4u.text.chess.boards.ChessBoard;
import main.java.com.lotos4u.text.chess.pieces.Elephant;
import main.java.com.lotos4u.text.chess.pieces.King;
import main.java.com.lotos4u.text.chess.pieces.Knight;
import main.java.com.lotos4u.text.chess.pieces.Piece;
import main.java.com.lotos4u.text.chess.pieces.Queen;
import main.java.com.lotos4u.text.chess.pieces.Rook;

public class Main {
    
    public static void main(String[] args) {
        test1();
        //test2();
        //testMisc();
    }

    /**
     * 1 Rook and 2 Kings on 3x3 Board
     */
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
        List<ChessBoard> boards = board.arrangePiecesVariants();
    }

    /**
     * 2 Rooks and 4 Knights on 4x4 Board
     */
    public static void test2(){
        Rook rook1 = new Rook();
        Rook rook2 = new Rook();
        Knight knight1 = new Knight();
        Knight knight2 = new Knight();
        Knight knight3 = new Knight();
        Knight knight4 = new Knight();
        ChessBoard board = new ChessBoard(4, 4);
        List<Piece> pieces = new ArrayList<Piece>();
        pieces.add(rook1);
        pieces.add(rook2);
        pieces.add(knight1);
        pieces.add(knight2);
        pieces.add(knight3);
        pieces.add(knight4);
        board.setPieces(pieces);
        
        board.arrangePiecesVariants();
        //List<ChessBoard> boards = board.arrangePiecesVariants(board);
    }
    
    public static void testMisc(){
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

        List<ChessBoard> boards = board.arrangePiecesVariants();
        //Log.out("There are " + boards.size() + " valid arrangements:");
    }


}
