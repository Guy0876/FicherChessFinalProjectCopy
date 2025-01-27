package com.example.ficherchess;

import com.example.ficherchess.Pieces.Piece;

public class Presenter {
    private IView view;
    private Model model;

    public Presenter(IView view) {
        this.view = view;
        this.model = new Model();
    }

    public void handlePieceSelection(int row, int col) {
        model.setSelectedPiece(row, col);
    }

    public void handlePieceMove(int oldRow, int oldCol, int newRow, int newCol) {
        if (model.isLegalMove(oldRow, oldCol, newRow, newCol)) {
            view.movePiece(oldRow, oldCol, newRow, newCol);
        }
    }
}
