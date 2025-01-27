package com.example.ficherchess.Pieces;

public class Queen extends Piece{

    public Queen(long bitboard, boolean isWhite) {
        super(bitboard, isWhite);
    }

    @Override
    public long possibleMoves(long position){
        long specificQueen = bitboard & position;
        long moves = 0L;

        moves |= horizontalMoves(specificQueen);
        moves |= verticalMoves(specificQueen);
        moves |= diagonalMoves(specificQueen);

        return moves;
    }

}
