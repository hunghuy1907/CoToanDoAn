package com.hungth.cotoan.screen.play_with_friend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    private Stack<Integer> stackChessBoards = new Stack<>();

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
        drawChess(canvas);
        drawGuideBoard(canvas);
    }

    public void drawChess(Canvas canvas) {
        for (int i = 0; i < chessBoardList.size(); i++) {
            ChessMan chessMan = chessBoardList.get(i).getChessMan();
            if (chessMan != null) {
                Rect rect = new Rect(chessMan.getmLeft(), chessMan.getmTop(),
                        chessMan.getmRight(), chessMan.getmBottom());
                canvas.drawBitmap(chessMan.getmBitmap(), null, rect, null);
            }
        }
    }

    public void drawGuideBoard(Canvas canvas) {
        if (chessBoardClick != null) {
            Rect rectBoard = new Rect(chessBoardClick.getLeft(), chessBoardClick.getTop(),
                    chessBoardClick.getRight(), chessBoardClick.getBottom());
            canvas.drawBitmap(chessBoardClick.getBitmap(), null, rectBoard, null);
        }
    }

    public ChessBoard getBoardClickChessman(int xClick, int yClick) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 9; j++) {
                ChessBoard chessBoard = chessBoardList.get(i * 9 + j);
                if (xClick >= chessBoard.getLeft()
                        && xClick < chessBoard.getRight()
                        && yClick >= chessBoard.getTop()
                        && yClick < chessBoard.getBottom()) {
                        stackChessBoards.push(i * 9 + j);
                    if (chessBoard.isClick()) {
                        chessBoard.setClick(false);
                    } else {
                        chessBoard.setClick(true);
                    }
                    if (chessBoard.isClick() && chessBoard.getChessMan() != null) {
                        return chessBoard;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int xClick = (int) event.getX();
            int yClick = (int) event.getY();
            chessBoardClick = getBoardClickChessman(xClick, yClick);
            if (stackChessBoards.size() >= 2) {
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
        int number0 = stackChessBoards.get(0);
        int number1 = stackChessBoards.get(1);
        System.out.println("--->>> 0: " + number0 + ", 1: " + number1);
        chessBoardList.get(number1).setChessMan(chessBoardList.get(number0).getChessMan());
        chessBoardList.get(number0).setChessMan(null);
//        if (chessBoardList.get(number0).getChessMan() != null
//                && chessBoardList.get(number1).getChessMan() == null) {
//            chessBoardList.get(number1).setChessMan(chessBoardList.get(number0).getChessMan());
//            chessBoardList.get(number0).setChessMan(null);
//        }
        invalidate();
    }
}
