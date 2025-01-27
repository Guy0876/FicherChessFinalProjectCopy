package com.example.ficherchess.Pieces;

public class Rooks extends Piece {

    public Rooks(long bitboard, boolean isWhite) {
        super(bitboard, isWhite);
    }

    @Override
    public long possibleMoves(long position) {
        long specificRook = bitboard & position;
        long moves = 0L;

        moves |= horizontalMoves(specificRook);
        moves |= verticalMoves(specificRook);

        return moves;
    }


}
