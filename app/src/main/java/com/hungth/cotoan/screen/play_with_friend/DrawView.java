package com.hungth.cotoan.screen.play_with_friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    private List<ChessMan> chessManRedList;
    private List<ChessMan> chessManBlueList;

    public void setChessBoardList(List<ChessBoard> chessBoardList) {
        this.chessBoardList = chessBoardList;
    }

    private List<ChessBoard> chessBoardList;
    private ChessBoard chessBoardClick;
    private List<Integer> stackChessBoards = new ArrayList<>();

    public DrawView(Context context) {
        super(context);
    }

    public void setChessManBlueList(List<ChessMan> chessManBlueList) {
        this.chessManBlueList = chessManBlueList;
    }

    public void setchessManRedList(List<ChessMan> chessManRedList) {
        this.chessManRedList = chessManRedList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (chessBoardClick != null) drawGuide(chessBoardClick.getChessMan().getValue(), canvas);
        drawChess(canvas);
    }

    public void drawChess(Canvas canvas) {
        int a = chessBoardList.size();
        for (int i = 0; i < a; i++) {
            ChessMan chessMan = chessBoardList.get(i).getChessMan();
            if (chessMan != null) {
                Rect rect = new Rect(chessMan.getmLeft(), chessMan.getmTop(), chessMan.getmRight(),
                         chessMan.getmBottom());
                canvas.drawBitmap(chessMan.getmBitmap(), null, rect, null);
            }
        }
    }

    public ChessBoard getChessmanClicked(int xClick, int yClick) {
        for (int i = 0; i < chessBoardList.size(); i++) {
            ChessBoard chessBoard = chessBoardList.get(i);
            if (xClick >= chessBoard.getLeft()
                    && xClick < chessBoard.getRight()
                    && yClick >= chessBoard.getTop()
                    && yClick < chessBoard.getBottom()) {
                stackChessBoards.add(i);
                if (chessBoard.isClick()) {
                    chessBoard.setClick(false);
                } else {
                    chessBoard.setClick(true);
                }
                if (chessBoard.getChessMan() != null) return chessBoard;
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int xClick = (int) event.getX();
            int yClick = (int) event.getY();
            chessBoardClick = getChessmanClicked(xClick, yClick);
            if (isMove()) {
                moveChessman();
            }
        }
        invalidate();
        return true;
    }

    public List<ChessBoard> getChessManInChessBoard(List<ChessBoard> chessBoards) {
        List<ChessMan> chessManList = new ArrayList<>();
        chessManList.addAll(chessManRedList);
        chessManList.addAll(chessManBlueList);
        for (int i = 0; i < chessBoards.size(); i++) {
            for (int j = 0; j < chessManList.size(); j++) {
                if (checkNotEmpty(chessManList.get(j), chessBoards.get(i))) {
                    chessBoards.get(i).setChessMan(chessManList.get(j));
                }
            }
        }
        return chessBoards;
    }

    private boolean checkNotEmpty(ChessMan chessMan, ChessBoard chessBoard) {
        return (chessMan.getmLeft() > chessBoard.getLeft()
                && chessMan.getmRight() < chessBoard.getRight()
                && chessMan.getmTop() > chessBoard.getTop()
                && chessMan.getmBottom() < chessBoard.getBottom());
    }

    private void moveChessman() {
        int numberNull = stackChessBoards.get(stackChessBoards.size() - 1);
        int numberChess = stackChessBoards.get(stackChessBoards.size() - 2);
        stackChessBoards.clear();
        if (chessBoardList.get(numberChess).getChessMan() != null
                && chessBoardList.get(numberNull).getChessMan() == null) {
            Bitmap bitmap = chessBoardList.get(numberChess).getChessMan().getmBitmap();
            int value = chessBoardList.get(numberChess).getChessMan().getValue();
            ChessBoard chessBoard = chessBoardList.get(numberNull);
            ChessMan chessMan = getChessmanToMove(chessBoard, bitmap, 41, value);
            chessBoardList.get(numberNull).setChessMan(chessMan);
            chessBoardList.get(numberChess).setChessMan(null);
            invalidate();
        }
    }

    public ChessMan getChessmanToMove(ChessBoard chessBoard, Bitmap bitmap, int type, int value) {
        return new ChessMan(chessBoard.getLeft() + 8, chessBoard.getRight() - 8,
                chessBoard.getTop() + 4, chessBoard.getBottom() - 4, bitmap, type, value);
    }

    public boolean isMove() {
        if (stackChessBoards.size() >= 2) {
            ChessBoard chessBoard = chessBoardList.get(stackChessBoards.get(stackChessBoards.size() - 1));
            if (chessBoard.getChessMan() == null) {
                return true;
            }
        }
        return false;
    }

    public void drawGuide(int value, Canvas canvas) {
        for (int i = 1; i <= value; i++) {
            for (int j = 0; j < chessBoardList.size(); j++) {
                ChessBoard chessBoard = chessBoardList.get(j);
                if (chessBoard.getChessMan() == null && value >= 1) {
                    if (checkChessmanCanGo(chessBoard, i)) {
                        Rect rectBoard = new Rect(chessBoard.getLeft() + 4, chessBoard.getTop() + 4,
                                chessBoard.getRight() - 4, chessBoard.getBottom() - 4);
                        canvas.drawBitmap(chessBoard.getBitmap(), null, rectBoard, null);
                    }
                }
            }
        }
    }

    public boolean checkChessmanCanGo(ChessBoard chessBoard, int value) {
        float widthBase = chessBoardList.get(1).getLeft() - chessBoardList.get(0).getLeft();
        float heightBase = chessBoardList.get(9).getTop() - chessBoardList.get(0).getTop();
        return (
                (chessBoard.getTop() == chessBoardClick.getTop() + heightBase * value
                        && chessBoard.getLeft() == chessBoardClick.getLeft()) || (chessBoard.getTop()
                        == chessBoardClick.getTop() - heightBase * value
                        && chessBoard.getLeft() == chessBoardClick.getLeft()) ||
                        (chessBoard.getLeft()
                                == chessBoardClick.getLeft() + widthBase * value
                                && chessBoard.getTop() == chessBoardClick.getTop()) || (chessBoard.getLeft()
                        == chessBoardClick.getLeft() - widthBase * value
                        && chessBoard.getTop() == chessBoardClick.getTop()));
    }
}
