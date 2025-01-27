package com.example.ficherchess.Pieces;

public class Knights extends Piece {
    public Knights(long bitboard, boolean isWhite) {
        super(bitboard , isWhite);
    }

    @Override
    public long possibleMoves(long position) {
        long specificKnight = bitboard & position;
        long myPieces = isWhite ? whitePieces : blackPieces;

        // Calculate all possible knight moves
        long move1 = (specificKnight & 0xFEFEFEFEFEFEFEFEL) << 15 & ~myPieces;
        long move2 = (specificKnight & 0xFCFCFCFCFCFCFCFCL) << 6 & ~myPieces;
        long move3 = (specificKnight & 0x7F7F7F7F7F7F7F7FL) << 17 & ~myPieces;
        long move4 = (specificKnight & 0x3F3F3F3F3F3F3F3FL) << 10 & ~myPieces;
        long move5 = (specificKnight & 0xFEFEFEFEFEFEFEFEL) >> 17 & ~myPieces;
        long move6 = (specificKnight & 0xFCFCFCFCFCFCFCFCL) >> 10 & ~myPieces;
        long move7 = (specificKnight & 0x7F7F7F7F7F7F7F7FL) >> 15 & ~myPieces;
        long move8 = (specificKnight & 0x3F3F3F3F3F3F3F3FL) >> 6 & ~myPieces;

        // Combine all possible moves
        return move1 | move2 | move3 | move4 | move5 | move6 | move7 | move8;
    }
}
