package com.example.ficherchess;

import com.example.ficherchess.Pieces.*;

import java.util.ArrayList;

public class Model {
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private long possibleMoves;
    private Piece selectedPiece;
    private boolean isWhiteTurn;

    public Model() {
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        possibleMoves = 0L;
        isWhiteTurn = true;
        initializeBoard();
    }
    public void initializeBoard() {
        whitePieces.add(new WhitePawns(0xFF000000000000L, true));
        whitePieces.add(new Knights(0x4200000000000000L, true));
        whitePieces.add(new Bishops(0x2400000000000000L, true));
        whitePieces.add(new Rooks(0x8100000000000000L, true));
        whitePieces.add(new Queen(0x800000000000000L, true));
        whitePieces.add(new King(0x1000000000000000L, true));
        blackPieces.add(new BlackPawns(0xFF00L, false));
        blackPieces.add(new Knights(0x42L, false));
        blackPieces.add(new Bishops(0x24L, false));
        blackPieces.add(new Rooks(0x81L, false));
        blackPieces.add(new Queen(0x8L, false));
        blackPieces.add(new King(0x10L, false));
    }
    public static long indexToBitboard(int row, int col) {
        int index = row * 8 + col;
        if (index < 0 || index > 63) {
            throw new IllegalArgumentException("Index must be between 0 and 63");
        }
        return 1L << index;
    }
    public void setSelectedPiece(int row, int col) {
        long specificPiece = indexToBitboard(row, col);
        possibleMoves = getPossibleMoves(specificPiece);
    }

    public boolean isLegalMove(int oldRow, int oldCol, int newRow, int newCol) {
        long movePosition = indexToBitboard(newRow, newCol);
        boolean isLegal = (possibleMoves & movePosition) != 0;
        if(isLegal) updateTurn(oldRow, oldCol, newRow, newCol);
        return isLegal;
    }

    private void updateTurn(int oldRow, int oldCol, int newRow, int newCol) {
        ArrayList<Piece> opponentPieces = isWhiteTurn ? blackPieces : whitePieces;
        long oldPosition = indexToBitboard(oldRow, oldCol);
        long newPosition = indexToBitboard(newRow, newCol);
        selectedPiece.setBitboard((selectedPiece.getBitboard() & ~oldPosition) | newPosition);
        Piece.allPieces &= ~oldPosition;
        Piece.allPieces |= newPosition;
        if(isWhiteTurn) {
            Piece.whitePieces &= ~oldPosition;
            Piece.whitePieces |= newPosition;
        } else {
            Piece.blackPieces &= ~oldPosition;
            Piece.blackPieces |= newPosition;
        }
        for (Piece opponentPiece : opponentPieces) {
            if ((opponentPiece.getBitboard() & newPosition) != 0) {
                opponentPiece.setBitboard(opponentPiece.getBitboard() & ~newPosition);
                if(isWhiteTurn) {
                    Piece.blackPieces &= ~newPosition;
                } else {
                    Piece.whitePieces &= ~newPosition;
                }
                break;
            }
        }
        isWhiteTurn = !isWhiteTurn;
        possibleMoves = 0L;
        selectedPiece = null;

        // Check if the move puts the opponent's king in check
        if (isKingInCheck(isWhiteTurn)) {
            System.out.println((isWhiteTurn ? "Black" : "White") + " king is in check!");
            Piece.check = true;
        }
    }

    public long getPossibleMoves(long specificPiece) {
        long moves = 0L;
        if(isWhiteTurn) {
            for (Piece piece : whitePieces) {
                if ((piece.getBitboard() & specificPiece) != 0) {
                    selectedPiece = piece;
                    moves = piece.possibleMoves(specificPiece);
                }
            }
        } else {
            for (Piece piece : blackPieces) {
                if ((piece.getBitboard() & specificPiece) != 0) {
                    selectedPiece = piece;
                    moves = piece.possibleMoves(specificPiece);
                }
            }
        }
        if(Piece.check) {
            moves = filterMovesThatResolveCheck(selectedPiece, moves, specificPiece);
        }
        return moves;
    }
    public long findKingPosition(boolean isWhite) {
        ArrayList<Piece> pieces = isWhite ? whitePieces : blackPieces;
        for (Piece piece : pieces) {
            if (piece instanceof King) {
                return piece.getBitboard();
            }
        }
        return 0L;
    }
    public boolean isKingInCheck(boolean isWhite) {
        long kingPosition = findKingPosition(isWhite);
        ArrayList<Piece> opponentPieces = isWhite ? blackPieces : whitePieces;
        for (Piece piece : opponentPieces) {
            if ((piece.possibleMoves(piece.getBitboard()) & kingPosition) != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean doesMoveResolveCheck(Piece piece, long movePosition, long specificPiece) {
        long originalPosition = piece.getBitboard();
        long originalAllPieces = Piece.allPieces;
        long originalWhitePieces = Piece.whitePieces;
        long originalBlackPieces = Piece.blackPieces;

        // Find the piece at the move position (if any)
        Piece capturedPiece = getPieceAtPosition(movePosition);

        // Perform the move
        piece.setBitboard((originalPosition & ~specificPiece) | movePosition);

        if (piece.isWhite()) {
            Piece.whitePieces = (originalWhitePieces & ~specificPiece) | movePosition;
        } else {
            Piece.blackPieces = (originalBlackPieces & ~specificPiece) | movePosition;
        }

        // Remove the captured piece from the board
        if (capturedPiece != null) {
            capturedPiece.setBitboard(capturedPiece.getBitboard() & ~movePosition);
            Piece.allPieces &= ~movePosition;
            if (capturedPiece.isWhite()) {
                Piece.whitePieces &= ~movePosition;
            } else {
                Piece.blackPieces &= ~movePosition;
            }
        }
        Piece.allPieces = (originalAllPieces & ~specificPiece) | movePosition;

        boolean isInCheck = isKingInCheck(piece.isWhite());

        // Revert the move
        piece.setBitboard(originalPosition);
        Piece.allPieces = originalAllPieces;
        Piece.whitePieces = originalWhitePieces;
        Piece.blackPieces = originalBlackPieces;

        // Restore the captured piece
        if (capturedPiece != null) {
            capturedPiece.setBitboard(movePosition);
            Piece.allPieces |= movePosition;
            if (capturedPiece.isWhite()) {
                Piece.whitePieces |= movePosition;
            } else {
                Piece.blackPieces |= movePosition;
            }
        }

        return !isInCheck;
    }

    public long filterMovesThatResolveCheck(Piece piece, long moves, long specificPiece) {
        long validMoves = 0L;
        for (int i = 0; i < 64; i++) {
            long movePosition = 1L << i;
            if ((moves & movePosition) != 0 && doesMoveResolveCheck(piece, movePosition, specificPiece)) {
                validMoves |= movePosition;
            }
        }
        return validMoves;
    }

    public Piece getPieceAtPosition(long position) {
        for (Piece piece : whitePieces) {
            if ((piece.getBitboard() & position) != 0) {
                return piece;
            }
        }
        for (Piece piece : blackPieces) {
            if ((piece.getBitboard() & position) != 0) {
                return piece;
            }
        }
        return null;
    }



}
