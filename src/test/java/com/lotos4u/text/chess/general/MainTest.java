package test.java.com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.com.lotos4u.text.chess.boards.ChessBoard;
import main.java.com.lotos4u.text.chess.general.Log;
import main.java.com.lotos4u.text.chess.pieces.Bishop;
import main.java.com.lotos4u.text.chess.pieces.King;
import main.java.com.lotos4u.text.chess.pieces.Knight;
import main.java.com.lotos4u.text.chess.pieces.Piece;
import main.java.com.lotos4u.text.chess.pieces.Queen;
import main.java.com.lotos4u.text.chess.pieces.Rook;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {
    /**
     * 2 Kings, 2 Queens, 2 Bishops and 1 Knight on 7×7 board
     */
	@Test public void testKKQQBBNon7x7(){
        
        King king1 = new King();
        King king2 = new King();
        Queen queen1 = new Queen();
        Queen queen2 = new Queen();
        Bishop bishop1 = new Bishop();
        Bishop bishop2 = new Bishop();
        Knight knight = new Knight();
        ChessBoard board = new ChessBoard(7, 7);
        List<Piece> pieces = new ArrayList<Piece>();
        
        pieces.add(king1);
        pieces.add(king2);
        pieces.add(queen1);
        pieces.add(queen2);
        pieces.add(bishop1);
        pieces.add(bishop2);
        pieces.add(knight);

        board.setPieces(pieces);
        List<ChessBoard> boards = board.arrangePiecesVariants();
        
        Assert.assertTrue(boards.size() > 0);
    }

    /**
     * 1 Rook and 2 Kings on 3x3 Board
     */
    @Test public void testRKKon3x3recursive(){
        Rook rook1 = new Rook();
        King king1 = new King();
        King king2 = new King();
        ChessBoard board = new ChessBoard(3, 3);
        List<Piece> pieces = new ArrayList<Piece>();
        pieces.add(rook1);
        pieces.add(king1);
        pieces.add(king2);
        board.setPieces(pieces);
        
        
        //List<ChessBoard> boards = board.arrangePiecesVariants();
        //List<ChessBoard> boards = board.arrangeRecursivelyVariants();
        //board.arrangeRecursivelyVariants();
        
        
        int validCOunter = 0;
        for (int i = 0; i < board.getPointsNumber(); i++) {
            for (int j = 0; j < board.getPiecesNumber(); j++) {
                Log.out("\n--- Point " + i + ", Piece " + j +  " ---");
                board.dropPieces();
                board.arrangeRecursively(i, j);
                if(board.isArrangedAndValid()){
                    //Log.out("============== Arrange Valid, pt=" + i + ", pi=" + j);
                    validCOunter++;
                }
            }
        }
        Log.out("\nValid counter = " + validCOunter);
        
        
        //board.dropPieces();
        
        //board.arrangeRecursively(2, 0);    
        
        //List<ChessBoard> boards = board.arrangeRecursivelyVariants();
        //Assert.assertEquals(4, boards.size());
    }
    
    /**
     * 1 Rook and 2 Kings on 3x3 Board
     */
    public void testRKKon3x3(){
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
        
        Assert.assertEquals(4, boards.size());
    }

    /**
     * 2 Rooks and 4 Knights on 4x4 Board
     */
    public void testRRNNNNon4x4(){
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
        
        List<ChessBoard> boards = board.arrangePiecesVariants();
        
        Assert.assertEquals(8, boards.size());
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

        Bishop elephant1 = new Bishop();
        Bishop elephant2 = new Bishop();
        Bishop elephant3 = new Bishop();
        Bishop elephant4 = new Bishop();

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
