package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Point;

public class WiseChessBoard extends ChessBoard {

    public WiseChessBoard(int xSize, int ySize, List<Piece> pieces) {
        super(xSize, ySize, pieces);
    }
    
    public WiseChessBoard(int xSize, int ySize) {
        super(xSize, ySize);
    }

   
    /**
     * Установить первую фигуру в первую клетку
     * 
     * @return
     */
    public List<List<Piece>> arrangePiecesStupidly(){
        List<List<Piece>> res = new ArrayList<List<Piece>>();
        
        //Initial pieces arrangement
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            piece.setPosition(points.get(i));
        }
        System.out.println("Initial arrangement: \n" + this.toString());
        System.out.println("Free: " + getPointsFree());
        System.out.println("Takeble: " + getPointsTakeble());
        
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

}
