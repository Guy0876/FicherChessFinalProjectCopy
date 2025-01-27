package com.example.ficherchess.Pieces;

public class Bishops extends Piece{
    public Bishops(long bitboard, boolean isWhite) {
        super(bitboard, isWhite);
    }

    @Override
    public long possibleMoves(long position){
        long specificBishop = bitboard & position;
        return diagonalMoves(specificBishop);
    }
}
