package com.hungth.cotoan.screen.play_with_friend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.hungth.cotoan.data.model.ChessMan;

import java.util.List;

public class DrawView extends View {

    private List<ChessMan> chessManRedList;
    private List<ChessMan> chessManBlueList;

    public DrawView(Context context) {
        super(context);
        init();
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

    Paint paint = new Paint();

    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < chessManRedList.size(); i++) {
            ChessMan chessManRed = chessManRedList.get(i);
            ChessMan chessManBlue = chessManBlueList.get(i);
            Rect rectRed = new Rect(chessManRed.getmLeft(), chessManRed.getmTop(), chessManRed.getmRight(), chessManRed.getmBottom());
            Rect rectBlue = new Rect(chessManBlue.getmLeft(), chessManBlue.getmTop(), chessManBlue.getmRight(), chessManBlue.getmBottom());
            canvas.drawBitmap(chessManRed.getmBitmap(), null, rectRed, null);
            canvas.drawBitmap(chessManBlue.getmBitmap(), null, rectBlue, null);
        }
    }
}
