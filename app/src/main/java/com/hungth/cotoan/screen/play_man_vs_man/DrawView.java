package com.hungth.cotoan.screen.play_man_vs_man;

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

    private List<ChessBoard> chessBoardList;
    private List<Integer> chessBoardRedAte = new ArrayList<>();
    private List<Integer> chessBoardBlueAte = new ArrayList<>();
    private ChessBoard chessBoardClick;
    private List<Integer> stackChessBoards = new ArrayList<>();
    private List<Integer> stackBackRed = new ArrayList<>();
    private List<Integer> stackBackBlue = new ArrayList<>();
    private int position;
    private boolean isBlueMove = true;
    private List<Integer> moves = new ArrayList<>();
    private List<Integer> chessmanCanEats = new ArrayList<>();
    private Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
            R.drawable.guide_60);
    private boolean isAdd = true, isSub = true, isMulti = true, isDiv = true;
    private int time, point;
    private IGameView iGameView;

    public DrawView(Context context, IGameView iGameView) {
        super(context);
        this.iGameView = iGameView;
    }

    public void setChessBoardList(List<ChessBoard> chessBoardList) {
        this.chessBoardList = chessBoardList;
    }

    public List<ChessBoard> getChessBoardList() {
        return chessBoardList;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
    }

    public void setDiv(boolean div) {
        isDiv = div;
    }

    public void setBlueMove(boolean blueMove) {
        isBlueMove = blueMove;
    }

    public boolean isBlueMove() {
        return isBlueMove;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPoint(int point) {
        this.point = point;
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
                int numberNull = stackChessBoards.get(stackChessBoards.size() - 1);
                int numberChess = stackChessBoards.get(stackChessBoards.size() - 2);
                moveChessman(numberNull, numberChess);
            }
        }
        invalidate();
        return true;
    }

    public void addListAte(ChessBoard chessBoard) {
        if (chessBoard.getChessMan() != null) {
            int value = chessBoard.getChessMan().getValue();
            int type = chessBoard.getChessMan().getmType();
            if (type == Constant.RED_NUMBER || type == Constant.RED_DOT) {
                chessBoardRedAte.add(value);
                iGameView.sendValueEnermyAte(chessBoardRedAte, chessBoard.getChessMan().getmType(),
                        getTotalPointRed());
            } else {
                chessBoardBlueAte.add(value);
                iGameView.sendValueEnermyAte(chessBoardBlueAte, chessBoard.getChessMan().getmType(),
                        getTotalPointBlue());
            }
            checkWin();
        }
    }

    public int getTotalPointRed() {
        int sum = 0;
        for (int i = 0; i < chessBoardRedAte.size(); i++) {
            sum = sum + chessBoardRedAte.get(i);
        }
        return sum;
    }

    public int getTotalPointBlue() {
        int sum = 0;
        for (int i = 0; i < chessBoardBlueAte.size(); i++) {
            sum = sum + chessBoardBlueAte.get(i);
        }
        return sum;
    }

    public void checkWin() {
        for (int i = 0; i < chessBoardRedAte.size(); i++) {
            if (chessBoardRedAte.get(i) == 0) {
                iGameView.showConfirmWin("ĐỎ");
            }
        }

        for (int i = 0; i < chessBoardBlueAte.size(); i++) {
            if (chessBoardBlueAte.get(i) == 0) {
                iGameView.showConfirmWin("XANH");
            }
        }

        if (getTotalPointBlue() >= point) {
            iGameView.showConfirmWin("XANH");
        }

        if (getTotalPointRed() >= point) {
            iGameView.showConfirmWin("ĐỎ");
        }
    }

    public void back() {
        if (stackBackRed.size() > 0 && stackBackBlue.size() > 0) {
            replace2Chessman(stackBackBlue.get(stackBackBlue.size() - 1), stackBackBlue.get(stackBackBlue.size() - 2));
            replace2Chessman(stackBackRed.get(stackBackRed.size() - 1), stackBackRed.get(stackBackRed.size() - 2));
            invalidate();
        }
        stackBackBlue.clear();
        stackBackRed.clear();
    }

    private void moveChessman(int numberNull, int numberChess) {
        stackChessBoards.clear();
        if (moves.contains(numberNull) && chessBoardList.get(numberChess).getChessMan() != null) {
            replace2Chessman(numberNull, numberChess);
            if (isBlueMove) {
                isBlueMove = false;
            } else {
                isBlueMove = true;
            }
            iGameView.sendTurn(isBlueMove);
            moves.clear();
            invalidate();
        }
    }

    public void replace2Chessman(int numberNull, int numberChess) {
        addListAte(chessBoardList.get(numberNull));
        Bitmap bitmap = chessBoardList.get(numberChess).getChessMan().getmBitmap();
        int value = chessBoardList.get(numberChess).getChessMan().getValue();
        int type = chessBoardList.get(numberChess).getChessMan().getmType();
        addBack(type, numberNull, numberChess);
        ChessBoard chessBoard = chessBoardList.get(numberNull);
        ChessMan chessMan = getChessmanToMove(chessBoard, bitmap, type, value);
        chessBoardList.get(numberNull).setChessMan(chessMan);
        chessBoardList.get(numberChess).setChessMan(null);
    }

    public void addBack(int type, int location1, int location2) {
        if (type == Constant.RED_DOT || type == Constant.RED_NUMBER) {
            stackBackRed.add(location1);
            stackBackRed.add(location2);
        } else {
            stackBackBlue.add(location1);
            stackBackBlue.add(location2);
        }
    }

    public ChessMan getChessmanToMove(ChessBoard chessBoard, Bitmap bitmap, int type, int value) {
        return new ChessMan(chessBoard.getLeft() + 8, chessBoard.getRight() - 8,
                chessBoard.getTop() + 12, chessBoard.getBottom() - 12, bitmap, type, value);
    }

    public boolean isMove() {
        if (stackChessBoards.size() >= 2) {
            moves.addAll(chessmanCanEats);
            ChessBoard chessBoard = chessBoardList.get(stackChessBoards.get(stackChessBoards.size() - 1));
            if (chessBoard.getChessMan() == null || chessmanCanEats.contains(stackChessBoards
                    .get(stackChessBoards.size() - 1))) {
                return true;
            }
        }
        return false;
    }

    public void basedrawGuide(Canvas canvas, int nextPosition) {
        ChessBoard chessBoard = chessBoardList.get(nextPosition);
        Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
        canvas.drawBitmap(bitmap, null, rectBoard, null);
        moves.add(nextPosition);

    }

    public void drawGuideVerticalTop(Canvas canvas) {
        for (int i = 1; i <= chessBoardClick.getChessMan().getValue(); i++) {
            if (position - 9 * i >= 0) {
                if (chessBoardList.get(position - 9 * i).getChessMan() == null) {
                    basedrawGuide(canvas, position - 9 * i);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position - 9).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardVerticalBottomticalTop(chessMan.getValue(), chessManNext.getValue()))) {
                        chessmanCanEats.add(position - 9 * getTotalChessboardVerticalBottomticalTop(chessMan.getValue(),
                                chessManNext.getValue()) - 9);
                    }
                    return;
                }
            }
        }
    }

    public void drawGuideVerticalBottom(Canvas canvas) {
        for (int i = 1; i <= chessBoardClick.getChessMan().getValue(); i++) {
            if (position + 9 * i < 99) {
                if (chessBoardList.get(position + 9 * i).getChessMan() == null) {
                    basedrawGuide(canvas, position + 9 * i);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position + 9).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardVerticalBottom(chessMan.getValue(),
                                    chessManNext.getValue()))) {
                        chessmanCanEats.add(position + 9 * getTotalChessboardVerticalBottom(chessMan.getValue(),
                                chessManNext.getValue()) + 9);
                    }
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
                    basedrawGuide(canvas, position - i);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position - 1).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardHorrizontalLeft(chessMan.getValue(),
                                    chessManNext.getValue()))) {
                        chessmanCanEats.add(position - getTotalChessboardHorrizontalLeft(chessMan.getValue(),
                                chessManNext.getValue()) - 1);
                    }
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
                    ChessBoard chessBoard = chessBoardList.get(position + i);
                    Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                            chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
                    canvas.drawBitmap(bitmap, null, rectBoard, null);
                    moves.add(position + i);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position + 1).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardHorrizontalRight(chessMan.getValue(),
                                    chessManNext.getValue()))) {
                        chessmanCanEats.add(position + getTotalChessboardHorrizontalRight(chessMan.getValue(),
                                chessManNext.getValue()) + 1);
                    }
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
            if (position - i * 8 >= 0) {
                if (chessBoardList.get(position - i * 8).getChessMan() == null) {
                    basedrawGuide(canvas, position - i * 8);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position - 8).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardCrossRightTop(chessMan.getValue(),
                                    chessManNext.getValue()))) {
                        chessmanCanEats.add(position - 8 * getTotalChessboardCrossRightTop(chessMan.getValue(),
                                chessManNext.getValue()) - 8);
                    }
                    return;
                }
            }
        }
    }

    public void drawGuideCrossLeftTop(Canvas canvas) {
        int row = position / 9;
        int count = position - row * 9;
        int size = chessBoardClick.getChessMan().getValue() > count ?
                count : chessBoardClick.getChessMan().getValue();
        for (int i = 1; i <= size; i++) {
            if (position - i * 10 >= 0) {
                if (chessBoardList.get(position - i * 10).getChessMan() == null) {
                    basedrawGuide(canvas, position - i * 10);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position - 10).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardCrossLeftTop(chessMan.getValue(),
                                    chessManNext.getValue()))) {
                        chessmanCanEats.add(position - 10 * getTotalChessboardCrossLeftTop(chessMan.getValue(),
                                chessManNext.getValue()) - 10);
                    }
                    return;
                }
            }
        }
    }

    public void drawGuideCrossRightBottom(Canvas canvas) {
        int row = position / 9;
        int count = (row + 1) * 9 - position - 1;
        int size = chessBoardClick.getChessMan().getValue() > count ?
                count : chessBoardClick.getChessMan().getValue();
        for (int i = 1; i <= size; i++) {
            if (position + i * 10 <= 99) {
                if (chessBoardList.get(position + i * 10).getChessMan() == null) {
                    basedrawGuide(canvas, position + i * 10);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position + 10).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardCrossRightBottom(chessMan.getValue(),
                                    chessManNext.getValue()))) {
                        chessmanCanEats.add(position + 10 * getTotalChessboardCrossRightBottom(chessMan.getValue(),
                                chessManNext.getValue()) + 10);
                    }
                    return;
                }
            }
        }
    }

    public void drawGuideCrossLeftBottom(Canvas canvas) {
        int row = position / 9;
        int count = position - row * 9;
        int size = chessBoardClick.getChessMan().getValue() > count ?
                count : chessBoardClick.getChessMan().getValue();
        for (int i = 1; i <= size; i++) {
            if (position + i * 8 < 99) {
                if (chessBoardList.get(position + i * 8).getChessMan() == null) {
                    basedrawGuide(canvas, position + i * 8);
                } else {
                    ChessMan chessManNext = chessBoardList.get(position + 8).getChessMan();
                    ChessMan chessMan = chessBoardClick.getChessMan();
                    if (i == 1 && checkSameType(chessMan, chessManNext)
                            && checkEat(chessMan.getValue(), chessManNext.getValue(),
                            getTotalChessboardCrossLeftBottom(chessMan.getValue(),
                                    chessManNext.getValue()))) {
                        chessmanCanEats.add(position + 8 * getTotalChessboardCrossLeftBottom(chessMan.getValue(),
                                chessManNext.getValue()) + 8);
                    }
                    return;
                }
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
            chessmanCanEats.clear();
            drawGuideVerticalTop(canvas);
            drawGuideHorizontalLeft(canvas);
            drawGuideHorizontalRight(canvas);
            drawGuideVerticalBottom(canvas);
            drawGuideCrossRightTop(canvas);
            drawGuideCrossRightBottom(canvas);
            drawGuideCrossLeftBottom(canvas);
            drawGuideCrossLeftTop(canvas);
            drawChessmanClick(canvas);
            drawGuideEatEnermy(canvas);
        }
    }

    public void drawGuideEatEnermy(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.guide_an_quan);
        for (int i = 0; i < chessmanCanEats.size(); i++) {
            ChessBoard chessBoard = chessBoardList.get(chessmanCanEats.get(i));
            Rect rectBoard = new Rect(chessBoard.getLeft(), chessBoard.getTop() + 6,
                    chessBoard.getRight() - 2, chessBoard.getBottom() - 5);
            canvas.drawBitmap(bitmap, null, rectBoard, null);
        }
    }

    public boolean checkEat(int valueClick, int valueNext, int totalChessboardToEnermy) {
        if (valueClick - valueNext == totalChessboardToEnermy && isSub)
            return true;
        int valueAdd = (valueClick + valueNext) >= 10 ?
                (valueClick + valueNext) % 10 : (valueClick + valueNext);
        if (valueAdd == totalChessboardToEnermy && isAdd)
            return true;

        if (valueClick > valueNext && valueNext != 0) {
            int valueDiv = valueClick / valueNext;
            if (valueDiv == totalChessboardToEnermy && isDiv)
                return true;
        }

        int valueMulti = (valueClick * valueNext) >= 10 ?
                (valueClick * valueNext) - ((valueClick * valueNext) / 10) * 10 : (valueClick * valueNext);
        if (valueMulti == totalChessboardToEnermy && valueMulti != 0 && isMulti)
            return true;
        return false;
    }

    public int getTotalChessboardVerticalBottomticalTop(int valueClick, int valueNext) {
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position - 9 * i >= 0) {
                ChessBoard chessBoard = chessBoardList.get(position - 9 * i);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public int getTotalChessboardVerticalBottom(int valueClick, int valueNext) {
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position + 9 * i < 99) {
                ChessBoard chessBoard = chessBoardList.get(position + 9 * i);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public int getTotalChessboardHorrizontalLeft(int valueClick, int valueNext) {
        int row = position / 9;
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position - i >= 9 * row) {
                ChessBoard chessBoard = chessBoardList.get(position - i);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public int getTotalChessboardHorrizontalRight(int valueClick, int valueNext) {
        int row = position / 9;
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position + i < 9 * (row + 1)) {
                ChessBoard chessBoard = chessBoardList.get(position + i);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public int getTotalChessboardCrossRightTop(int valueClick, int valueNext) {
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position - i * 8 >= 0) {
                ChessBoard chessBoard = chessBoardList.get(position - i * 8);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
        return -1;
    }

    public int getTotalChessboardCrossLeftTop(int valueClick, int valueNext) {
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position - i * 10 >= 0) {
                ChessBoard chessBoard = chessBoardList.get(position - i * 10);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public int getTotalChessboardCrossLeftBottom(int valueClick, int valueNext) {
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position + i * 8 < 99) {
                ChessBoard chessBoard = chessBoardList.get(position + i * 8);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public int getTotalChessboardCrossRightBottom(int valueClick, int valueNext) {
        int max = getMax(valueClick, valueNext);
        for (int i = 2; i < max + 2; i++) {
            if (position + i * 10 <= 99) {
                ChessBoard chessBoard = chessBoardList.get(position + i * 10);
                if (chessBoard.getChessMan() != null) {
                    if (!checkSameType(chessBoard.getChessMan(), chessBoardClick.getChessMan())) {
                        return i - 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public boolean checkSameType(ChessMan chessMan, ChessMan chessMan1) {
        if (chessMan.getmType() == Constant.BLUE_DOT || chessMan.getmType() == Constant.BLUE_NUMBER) {
            return (chessMan1.getmType() == Constant.BLUE_DOT || chessMan1.getmType() == Constant.BLUE_NUMBER);
        } else {
            return (chessMan1.getmType() == Constant.RED_DOT || chessMan1.getmType() == Constant.RED_NUMBER);
        }
    }

    public int getMax(int valueClick, int valueNext) {
        int max = -10000;
        List<Integer> calculator = new ArrayList<>();
        int add = (valueClick + valueNext) >= 10 ?
                (valueClick + valueNext) % 10 : (valueClick + valueNext);
        int minus = valueClick - valueNext;
        int multy = (valueClick * valueNext) - ((valueClick * valueNext) / 10) * 10;
        int sub = 0;
        calculator.add(add);
        calculator.add(minus);
        calculator.add(multy);
        calculator.add(sub);
        for (int i = 0; i < calculator.size(); i++) {
            if (max < calculator.get(i))
                max = calculator.get(i);
        }
        return max;
    }

    public void setNewGame() {
        stackBackRed.clear();
        stackBackBlue.clear();
    }
}
