package com.hungth.cotoan.screen.play_with_friend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;

import java.util.List;

public class DrawView extends View {

    private List<ChessMan> chessManRedList;
    private List<ChessMan> chessManBlueList;

    public List<ChessBoard> getChessBoardList() {
        return chessBoardList;
    }

    public void setChessBoardList(List<ChessBoard> chessBoardList) {
        this.chessBoardList = chessBoardList;
    }

    private List<ChessBoard> chessBoardList;
    private ChessBoard chessBoardClick;

    public DrawView(Context context) {
        super(context);
    }

    public List<ChessMan> getChessManBlueList() {
        return chessManBlueList;
    }

    public void setChessManBlueList(List<ChessMan> chessManBlueList) {
        this.chessManBlueList = chessManBlueList;
    }

    public List<ChessMan> getchessManRedList() {
        return chessManRedList;
    }

    public void setchessManRedList(List<ChessMan> chessManRedList) {
        this.chessManRedList = chessManRedList;
    }

    public ChessBoard getChessBoardClick() {
        return chessBoardClick;
    }

    public void setChessBoardClick(ChessBoard chessBoardClick) {
        this.chessBoardClick = chessBoardClick;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChessmans(canvas);
        drawGuideBoard(canvas);
    }

    public void drawChessmans(Canvas canvas) {
        for (int i = 0; i < chessManRedList.size(); i++) {
            ChessMan chessManRed = chessManRedList.get(i);
            ChessMan chessManBlue = chessManBlueList.get(i);
            Rect rectRed = new Rect(chessManRed.getmLeft(), chessManRed.getmTop(),
                    chessManRed.getmRight(), chessManRed.getmBottom());
            Rect rectBlue = new Rect(chessManBlue.getmLeft(), chessManBlue.getmTop(),
                    chessManBlue.getmRight(), chessManBlue.getmBottom());
            canvas.drawBitmap(chessManRed.getmBitmap(), null, rectRed, null);
            canvas.drawBitmap(chessManBlue.getmBitmap(), null, rectBlue, null);
        }
    }

    public void drawGuideBoard(Canvas canvas) {
        if (chessBoardClick != null) {
            Rect rectBoard = new Rect(chessBoardClick.getLeft(), chessBoardClick.getTop(),
                    chessBoardClick.getRight(), chessBoardClick.getBottom());
            canvas.drawBitmap(chessBoardClick.getBitmap(), null, rectBoard, null);
        }
    }

    public ChessBoard getBoardClick(int xClick, int yClick) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 9; j++) {
                ChessBoard chessBoard = chessBoardList.get(i * 9 + j);
                if (xClick >= chessBoard.getLeft()
                        && xClick < chessBoard.getRight()
                        && yClick >= chessBoard.getTop()
                        && yClick < chessBoard.getBottom()) {
                    return chessBoard;
                }
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xClick = (int) event.getX();
        int yClick = (int) event.getY();
        chessBoardClick = getBoardClick(xClick, yClick);
        invalidate();
        return true;
    }
}
