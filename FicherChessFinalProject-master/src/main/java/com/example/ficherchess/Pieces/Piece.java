package com.example.ficherchess.Pieces;
import com.example.ficherchess.Model;
public abstract class Piece {
    protected long bitboard;
    public static long allPieces;
    public static long whitePieces;
    public static long blackPieces;
    private static Model model;
    protected boolean isWhite;
    public static boolean check;

    public Piece(long bitboard, boolean isWhite) {
        this.bitboard = bitboard;
        Piece.allPieces |= bitboard;
        Piece.check = false;
        this.isWhite = isWhite;
        if (isWhite) {
            Piece.whitePieces |= bitboard;
        } else {
            Piece.blackPieces |= bitboard;
        }
    }

    public long getAllPieces() {
        return allPieces;
    }

    public void setAllPieces(long allPieces) {
        Piece.allPieces |= allPieces;
    }
    public void setCheck() {
        Piece.check = true;
    }
    public long getBitboard() {
        return bitboard;
    }

    public void setBitboard(long bitboard) {
        this.bitboard = bitboard;
    }

    public abstract long possibleMoves(long position);

    protected long horizontalMoves(long specificPiece) {
        long moves = 0L;
        long temp = specificPiece;
        long myPieces = isWhite ? whitePieces : blackPieces;

        // Move left
        while ((temp & 0x7F7F7F7F7F7F7F7FL) != 0) {
            temp <<= 1;
            if((temp & myPieces) != 0) break;
            moves |= temp;
            if ((temp & allPieces) != 0) break;
        }

        temp = specificPiece;

        // Move right
        while ((temp & 0xFEFEFEFEFEFEFEFEL) != 0) {
            temp >>= 1;
            if((temp & myPieces) != 0) break;
            moves |= temp;
            if ((temp & allPieces) != 0) break;
        }

        return moves;
    }

    protected long verticalMoves(long specificPiece) {
        long moves = 0L;
        long temp = specificPiece;
        long myPieces = isWhite ? whitePieces : blackPieces;

        // Move up
        while ((temp & 0x00FFFFFFFFFFFFFFL) != 0) {
            temp <<= 8;
            if((temp & myPieces) != 0) break;
            moves |= temp;
            if ((temp & allPieces) != 0) break;
        }

        temp = specificPiece;

        // Move down
        while ((temp & 0xFFFFFFFFFFFFFF00L) != 0) {
            temp >>= 8;
            if((temp & myPieces) != 0) break;
            moves |= temp;
            if ((temp & allPieces) != 0) break;
        }

        return moves;
    }

    protected long diagonalMoves(long specificPiece) {
        long Moves = 0L;
        long temp = specificPiece;
        long myPieces = isWhite ? whitePieces : blackPieces;

        // Move up-right
        while((temp & 0x00FFFFFFFFFFFFFFL) != 0 && (temp & 0x0101010101010101L) == 0){
            temp <<= 7;
            if((temp & myPieces) != 0) break;
            Moves |= temp;
            if((temp & Piece.allPieces) != 0) break;
        }

        temp = specificPiece;

        // Move up-left
        while((temp & 0x00FFFFFFFFFFFFFFL) != 0 && (temp & 0x8080808080808080L) == 0){
            temp <<= 9;
            if((temp & myPieces) != 0) break;
            Moves |= temp;
            if((temp & Piece.allPieces) != 0) break;
        }

        temp = specificPiece;

        // Move down-right
        while((temp & 0xFFFFFFFFFFFFFF00L) != 0 && (temp & 0x0101010101010101L) == 0){
            temp >>= 9;
            if((temp & myPieces) != 0) break;
            Moves |= temp;
            if((temp & Piece.allPieces) != 0) break;
        }

        temp = specificPiece;

        // Move down-left
        while((temp & 0xFFFFFFFFFFFFFF00L) != 0 && (temp & 0x8080808080808080L) == 0){
            temp >>= 7;
            if((temp & myPieces) != 0) break;
            Moves |= temp;
            if((temp & Piece.allPieces) != 0) break;
        }

        return Moves;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
