package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.lotos4u.text.chess.boards.ChessBoard;
import com.lotos4u.text.chess.pieces.Bishop;
import com.lotos4u.text.chess.pieces.King;
import com.lotos4u.text.chess.pieces.Knight;
import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Queen;
import com.lotos4u.text.chess.pieces.Rook;

public class MainTest {
	
	@Test @Ignore
	public void testPieceSameKind() {
        Log.out("\n\n********************** Test testPieceSameKind **********************\n");
        King king1 = new King();
        King king2 = new King();
        Queen queen1 = new Queen();
        Queen queen2 = new Queen();
        Bishop bishop1 = new Bishop();
        Bishop bishop2 = new Bishop();
        
        Log.out(king2.isSameKind(king1));
        Log.out(king2.isSameKind(queen1));
		
	}
    /**
     * 2 Kings, 2 Queens, 2 Bishops and 1 Knight on 7×7 board
     */
	@Test @Ignore
	public void testKKQQBBNon7x7(){
        Log.out("\n\n********************** Test testKKQQBBNon7x7 **********************\n");
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
        int boards = board.arrangeRecursivelyVariants();
        
        Assert.assertTrue(boards > 0);
    }

    /**
     * 1 Rook and 2 Kings on 3x3 Board
     */
    @Test @Ignore
    public void testRKKon3x3(){
    	Log.out("\n\n********************** Test testRKKon3x3 **********************\n");
        Rook rook1 = new Rook();
        King king1 = new King();
        King king2 = new King();
        ChessBoard board = new ChessBoard(3, 3);
        List<Piece> pieces = new ArrayList<Piece>();
        pieces.add(rook1);
        pieces.add(king1);
        pieces.add(king2);
        board.setPieces(pieces);
        int res = board.arrangeRecursivelyVariants();
        
        Assert.assertEquals(8, res);
    }

    
    /**
     * 2 Rooks and 4 Knights on 4x4 Board
     */
    @Test
    public void testRRNNNNon4x4(){
    	Log.out("\n\n********************** Test testRRNNNNon4x4 **********************\n");
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
        
        int res = board.arrangeRecursivelyVariants();
        
        Assert.assertEquals(8, res);
    }

}
