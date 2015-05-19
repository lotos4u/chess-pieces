package com.lotos4u.tests.chess.general;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.lotos4u.text.chess.boards.ChessBoard;
import com.lotos4u.text.chess.boards.Point;
import com.lotos4u.text.chess.general.Log;
import com.lotos4u.text.chess.pieces.Bishop;
import com.lotos4u.text.chess.pieces.King;
import com.lotos4u.text.chess.pieces.Knight;
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
	@Test
	public void testKKQQBBNon7x7(){
        Log.out("\n\n********************** Test testKKQQBBNon7x7 **********************\n");
        ChessBoard board = new ChessBoard(7, 7);
        board.addPiece(new King());
        board.addPiece(new King());
        board.addPiece(new Queen());
        board.addPiece(new Queen());
        board.addPiece(new Bishop());
        board.addPiece(new Bishop());
        board.addPiece(new Knight());
        
        int boards = board.arrangeRecursivelyVariants();
        
        Assert.assertTrue(boards > 0);
    }

	@Test @Ignore
	public void testSameArrange() {
		Log.out("\n\n********************** Test testSameArrange **********************\n");
		ChessBoard board1 = new ChessBoard(3, 3);
		board1.addPiece(new Rook());
		board1.addPiece(new Rook());
		board1.getPiece(1).setPosition(new Point(1, 1));
		board1.getPiece(0).setPosition(new Point(1, 2));
		Log.out(board1);
		
		ChessBoard board2 = new ChessBoard(3, 3);
		board2.addPiece(new Rook());
		board2.addPiece(new Rook());
		board2.getPiece(0).setPosition(new Point(1, 1));
		board2.getPiece(1).setPosition(new Point(1, 2));
		Log.out(board2);
		
		Log.out(board1.equals(board2));
		Log.out("H1=" + board1.hashCode() + ", H2=" + board2.hashCode());
		
		Set<ChessBoard> boards = new HashSet<ChessBoard>();
		boards.add(board1);
		boards.add(board2);
		Log.out(boards.size());
	}
    /**
     * 1 Rook and 2 Kings on 3x3 Board
     */
    @Test
    public void testRKKon3x3(){
    	Log.out("\n\n********************** Test testRKKon3x3 **********************\n");
    	ChessBoard board = new ChessBoard(3, 3);
    	board.addPiece(new Rook());
    	board.addPiece(new King());
    	board.addPiece(new King());
        
        int res = board.arrangeRecursivelyVariants();
        
        Assert.assertEquals(4, res);
    }

    
    /**
     * 2 Rooks and 4 Knights on 4x4 Board
     */
    @Test
    public void testRRNNNNon4x4(){
    	Log.out("\n\n********************** Test testRRNNNNon4x4 **********************\n");
    	ChessBoard board = new ChessBoard(4, 4);
    	board.addPiece(new Rook());
    	board.addPiece(new Rook());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
        
        int res = board.arrangeRecursivelyVariants();
        
        Assert.assertEquals(8, res);
    }

}
