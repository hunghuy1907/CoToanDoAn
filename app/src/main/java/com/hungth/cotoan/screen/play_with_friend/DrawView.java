package com.hungth.cotoan.screen.play_with_friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.utils.Constant;

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
    private int position;
    private boolean isBlueMove = true;
    private List<Integer> moves = new ArrayList<>();

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
        drawChess(canvas);
        drawGuide(canvas);
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
                if (chessBoard.getChessMan() != null) {
                    position = i;
                    if (isBlueMove) {
                        if (chessBoard.getChessMan().getmType() == Constant.RED_NUMBER ||
                                chessBoard.getChessMan().getmType() == Constant.RED_DOT) {
                            return null;
                        }
                    } else {
                        if (chessBoard.getChessMan().getmType() == Constant.BLUE_NUMBER ||
                                chessBoard.getChessMan().getmType() == Constant.BLUE_DOT) {
                            return null;
                        }
                    }
                    return chessBoard;
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
        if (moves.contains(numberNull) && chessBoardList.get(numberChess).getChessMan() != null
                && chessBoardList.get(numberNull).getChessMan() == null) {
            Bitmap bitmap = chessBoardList.get(numberChess).getChessMan().getmBitmap();
            int value = chessBoardList.get(numberChess).getChessMan().getValue();
            int type = chessBoardList.get(numberChess).getChessMan().getmType();
            ChessBoard chessBoard = chessBoardList.get(numberNull);
            ChessMan chessMan = getChessmanToMove(chessBoard, bitmap, type, value);
            chessBoardList.get(numberNull).setChessMan(chessMan);
            chessBoardList.get(numberChess).setChessMan(null);
            if (isBlueMove) {
                isBlueMove = false;
            } else {
                isBlueMove = true;
            }
            invalidate();
        }
    }

    public ChessMan getChessmanToMove(ChessBoard chessBoard, Bitmap bitmap, int type, int value) {
        return new ChessMan(chessBoard.getLeft() + 8, chessBoard.getRight() - 8,
                chessBoard.getTop() + 12, chessBoard.getBottom() - 12, bitmap, type, value);
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

    public void drawBaseGuide(Canvas canvas, int nextPosition){
        ChessBoard chessBoard = chessBoardList.get(nextPosition);
        if (chessBoard.getChessMan() == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.guide);
            chessBoard.setBitmap(bitmap);
            Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                    chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
            canvas.drawBitmap(chessBoard.getBitmap(), null, rectBoard, null);
            moves.add(nextPosition);
        } else {
            return;
        }
    }

    public void drawGuideVerticalTop(Canvas canvas) {
        for (int i = 1; i <= chessBoardClick.getChessMan().getValue(); i++) {
            if (position - 9 * i >= 0) {
                if (chessBoardList.get(position - 9 * i).getChessMan() == null) {
                    ChessBoard chessBoard = chessBoardList.get(position - 9 * i);
                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.guide);
                    Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                            chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                    canvas.drawBitmap(bitmap, null, rectBoard, null);
                    moves.add(position - 9 * i);
                } else {
                    return;
                }
            }
        }
    }

    public void drawGuideVerticalBottom(Canvas canvas) {
        for (int i = 1; i <= chessBoardClick.getChessMan().getValue(); i++) {
            if (position + 9 * i < 99) {
                if (chessBoardList.get(position + 9 * i).getChessMan() == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.guide);
                    ChessBoard chessBoard = chessBoardList.get(position + 9 * i);
                    Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                            chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                    canvas.drawBitmap(bitmap, null, rectBoard, null);
                    moves.add(position + 9 * i);
                } else {
                    return;
                }
            }
        }
    }

    public void drawGuideHorizontalLeft(Canvas canvas) {
        int row = position / 9;
        for (int i = 1; i <= chessBoardClick.getChessMan().getValue(); i++) {
            if (position - i >= 9 * row) {
                if (chessBoardList.get(position - i).getChessMan() == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.guide);
                    ChessBoard chessBoard = chessBoardList.get(position - i);
                    Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                            chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                    canvas.drawBitmap(bitmap, null, rectBoard, null);
                    moves.add(position - i);
                } else {
                    return;
                }
            }
        }
    }

    public void drawGuideHorizontalRight(Canvas canvas) {
        int row = position / 9;
        for (int i = 1; i <= chessBoardClick.getChessMan().getValue(); i++) {
            if (position + i < 9 * (row + 1)) {
                if (chessBoardList.get(position + i).getChessMan() == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.guide);
                    ChessBoard chessBoard = chessBoardList.get(position + i);
                    Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                            chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                    canvas.drawBitmap(bitmap, null, rectBoard, null);
                    moves.add(position + i);
                } else {
                    return;
                }
            }
        }
    }

    public void drawGuideCrossRightTop(Canvas canvas) {
        int row = position / 9;
        int count = (row + 1) * 9 - position - 1;
        int size = chessBoardClick.getChessMan().getValue() > count ?
                count : chessBoardClick.getChessMan().getValue();
        for (int i = 1; i <= size; i++) {
            if (position - i * 8 >= 0 && chessBoardList.get(position - i * 8).getChessMan() == null) {
                ChessBoard chessBoard = chessBoardList.get(position - i * 8);
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.guide);
                chessBoard.setBitmap(bitmap);
                Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                        chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                canvas.drawBitmap(chessBoard.getBitmap(), null, rectBoard, null);
                moves.add(position - i * 8);
            } else {
                return;
            }

        }
    }

    public void drawGuideCrossLeftTop(Canvas canvas) {
        int row = position / 9;
        int count = position - row * 9;
        int size = chessBoardClick.getChessMan().getValue() > count ?
                count : chessBoardClick.getChessMan().getValue();
        for (int i = 1; i <= size; i++) {
            if (position - i * 10 >= 0 && chessBoardList.get(position - i * 10).getChessMan() == null) {
                ChessBoard chessBoard = chessBoardList.get(position - i * 10);
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.guide);
                chessBoard.setBitmap(bitmap);
                Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                        chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                canvas.drawBitmap(chessBoard.getBitmap(), null, rectBoard, null);
                moves.add(position - i * 10);
            } else {
                return;
            }

        }
    }

    public void drawGuideCrossRightBottom(Canvas canvas) {
        int row = position / 9;
        int count = (row + 1) * 9 - position - 1;
        int size = chessBoardClick.getChessMan().getValue() > count ?
                count : chessBoardClick.getChessMan().getValue();
        for (int i = 1; i <= size; i++) {
            if (position + i * 10 <= 99 && chessBoardList.get(position + i * 10).getChessMan() == null) {
                ChessBoard chessBoard = chessBoardList.get(position + i * 10);
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.guide);
                chessBoard.setBitmap(bitmap);
                Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                        chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                canvas.drawBitmap(chessBoard.getBitmap(), null, rectBoard, null);
                moves.add(position + i * 10);
            } else {
                return;
            }

        }
    }

    public void drawGuideCrossLeftBottom(Canvas canvas) {
        int row = position / 9;
        int count = position - row * 9;
        int size = chessBoardClick.getChessMan().getValue() > count ?
                count : chessBoardClick.getChessMan().getValue();
        for (int i = 1; i <= size; i++) {
            if (position + i * 8 < 99 && chessBoardList.get(position + i * 8).getChessMan() == null) {
                ChessBoard chessBoard = chessBoardList.get(position + i * 8);
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.guide);
                chessBoard.setBitmap(bitmap);
                Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                        chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                canvas.drawBitmap(chessBoard.getBitmap(), null, rectBoard, null);
                moves.add(position + i * 8);
            } else {
                return;
            }

        }
    }

    public void drawChessmanClick(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.guide_chess_is_move);
        chessBoardClick.setBitmap(bitmap);
        Rect rectBoard = new Rect(chessBoardClick.getLeft(), chessBoardClick.getTop() + 6,
                chessBoardClick.getRight() - 2, chessBoardClick.getBottom() - 5);
        canvas.drawBitmap(chessBoardClick.getBitmap(), null, rectBoard, null);
    }

    public void drawGuide(Canvas canvas) {
        if (chessBoardClick != null) {
            moves.clear();
            drawGuideVerticalTop(canvas);
            drawGuideHorizontalLeft(canvas);
            drawGuideHorizontalRight(canvas);
            drawGuideVerticalBottom(canvas);
            drawGuideCrossRightTop(canvas);
            drawGuideCrossRightBottom(canvas);
            drawGuideCrossLeftBottom(canvas);
            drawGuideCrossLeftTop(canvas);
            drawChessmanClick(canvas);
        }
    }

//    public void drawGuideEatChessman(Canvas canvas) {
//        int nextChessboard
//    }
}
