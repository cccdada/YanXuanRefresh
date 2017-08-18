package com.sheng.yanxuanrefresh.refresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.sheng.yanxuanrefresh.R;

/**
 * @author wangsheng 1.0.0 第一版效果基本有了 但是需要调整
 *
 * todo 一些位置计算待完善
 *
 *         根据角度计算圆上某点的坐标
 *         圆点坐标：(x0,y0)
 *         半径：r
 *         角度：angle
 *         则圆上任一点为：（x1,y1）
 *         x1   =   x0   +   r   *   cos(angle   *   3.14   /180   )
 *         y1   =   y0   +   r   *   sin(angle   *   3.14   /180   )
 */

public class YanXuanRefreshView extends View {
    Paint paint1;
    //盒子上方的角的路径
    Path leftPath = new Path();
    Path rightPath = new Path();

    //盒子的两个盖子的上方的角坐标
    Point pointLeftOut;
    Point pointLeftInner;
    Point pointRightOut;
    Point pointRightInner;

    //盒子上方的四个角相当于四个圆心  盖子相当于围绕圆心做运动
    Point lCCPointOut;
    Point lCCPointInner;
    Point rCCPointOut;
    Point rCCPointInner;

    Path path1 = new Path();
    Path path2 = new Path();
    Path path3 = new Path();
    private int viewSizeHeight;
    private int viewSizeWidth;

    //标语字的集合
    private Bitmap[] slogan;
    //字坐标的集合
    private Point[] points;
    //严选logo
    private Bitmap yxLogo;
    //阴影的矩形
    private RectF rectShadow;

    private int jumpInt;
    private int shakeInt;
    private ViewStatus viewStatus;

    //当前的下拉距离
    int distance = 0;
    int leftAngle = -0;
    int rightAngle = Math.abs(leftAngle) - 180;

    private int outCircleR = 100;
    private int innerCircleR = 70;

    public YanXuanRefreshView(Context context) {
        this(context, null);
    }

    public YanXuanRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YanXuanRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        //初始所有的点
        pointLeftOut = new Point(0, 0);
        pointLeftInner = new Point(0, 0);
        pointRightOut = new Point(0, 0);
        pointRightInner = new Point(0, 0);
        lCCPointOut = new Point(0, 0);
        lCCPointInner = new Point(0, 0);
        rCCPointOut = new Point(0, 0);
        rCCPointInner = new Point(0, 0);

        //获取所有的字
        int[] arrayOfInt = {R.mipmap.all_refresh_hao_ic, R.mipmap.all_refresh_de_ic, R.mipmap.all_refresh_sheng_ic, R.mipmap.all_refresh_huo_ic
                , R.mipmap.all_refresh_mei_ic, R.mipmap.all_refresh_na_ic, R.mipmap.all_refresh_me_ic, R.mipmap.all_refresh_gui_ic};
        slogan = new Bitmap[arrayOfInt.length];
        points = new Point[arrayOfInt.length];
        for (int i = 0; i < arrayOfInt.length; i++) {
            slogan[i] = BitmapFactory.decodeResource(getResources(), arrayOfInt[i]);
            points[i] = new Point();
        }

        yxLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.all_refresh_yanxuan_ic);

        rectShadow = new RectF();
        viewStatus = ViewStatus.START;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewSizeHeight = getMeasuredHeight();
        viewSizeWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        lCCPointOut.pointX = viewSizeWidth / 2 - outCircleR;
        lCCPointOut.pointY = viewSizeHeight / 2;
        lCCPointInner.pointX = viewSizeWidth / 2 - innerCircleR;
        lCCPointInner.pointY = viewSizeHeight / 2 - 30;
        rCCPointOut.pointX = viewSizeWidth / 2 + outCircleR;
        rCCPointOut.pointY = viewSizeHeight / 2;
        rCCPointInner.pointX = viewSizeWidth / 2 + innerCircleR;
        rCCPointInner.pointY = viewSizeHeight / 2 - 30;

        pointLeftOut.setPointX((float) (lCCPointOut.pointX + outCircleR * Math.cos(leftAngle * 3.14 / 180)));
        pointLeftOut.setPointY((float) (lCCPointOut.pointY + outCircleR * Math.sin(leftAngle * 3.14 / 180)));

        pointLeftInner.setPointX((float) (lCCPointInner.pointX + innerCircleR * Math.cos(leftAngle * 3.14 / 180)));
        pointLeftInner.setPointY((float) (lCCPointInner.pointY + innerCircleR * Math.sin(leftAngle * 3.14 / 180)));

        pointRightOut.setPointX((float) (rCCPointOut.pointX + outCircleR * Math.cos(rightAngle * 3.14 / 180)));
        pointRightOut.setPointY((float) (rCCPointOut.pointY + outCircleR * Math.sin(rightAngle * 3.14 / 180)));

        pointRightInner.setPointX((float) (rCCPointInner.pointX + innerCircleR * Math.cos(rightAngle * 3.14 / 180)));
        pointRightInner.setPointY((float) (rCCPointInner.pointY + innerCircleR * Math.sin(rightAngle * 3.14 / 180)));

        paint1.setAntiAlias(true);
        paint1.setColor(Color.parseColor("#FF7C7C7C"));
        //绘制第一个梯形
        path1.moveTo(lCCPointInner.pointX, lCCPointInner.pointY);
        path1.lineTo(rCCPointInner.pointX, rCCPointInner.pointY);
        path1.lineTo(rCCPointOut.pointX, rCCPointOut.pointY);
        path1.lineTo(lCCPointOut.pointX, lCCPointOut.pointY);
        path1.close();
        canvas.drawPath(path1, paint1);

        //绘制内侧矩形
        paint1.setColor(Color.parseColor("#FF979797"));
        path2.moveTo(lCCPointInner.pointX, lCCPointInner.pointY);
        path2.lineTo(rCCPointInner.pointX, rCCPointInner.pointY);
        path2.lineTo(rCCPointInner.pointX, rCCPointOut.pointY);
        path2.lineTo(lCCPointInner.pointX, lCCPointOut.pointY);
        path2.close();
        canvas.drawPath(path2, paint1);

        drawCover(canvas);
        drawSlogan(canvas);

        //绘制阴影
        paint1.setColor(Color.parseColor("#FFDCDCDE"));
        rectShadow.left = lCCPointOut.pointX - 50;
        rectShadow.right = rCCPointOut.pointX + 50;
        rectShadow.top = lCCPointOut.pointY + 80;
        rectShadow.bottom = lCCPointOut.pointY + 120;
        canvas.drawOval(rectShadow, paint1);

        //绘制下方的矩形
        paint1.setColor(Color.parseColor("#FFC6C6C6"));
        path3.moveTo(lCCPointOut.pointX, lCCPointOut.pointY);
        path3.lineTo(rCCPointOut.pointX, rCCPointOut.pointY);
        path3.lineTo(rCCPointOut.pointX, rCCPointOut.pointY + 100);
        path3.lineTo(lCCPointOut.pointX, lCCPointOut.pointY + 100);
        path3.close();
        canvas.drawPath(path3, paint1);
        paint1.reset();

        //绘制严选文字logo
        canvas.drawBitmap(yxLogo, lCCPointOut.pointX + 100 - yxLogo.getWidth() / 2, lCCPointOut.pointY + 50 - yxLogo.getHeight() / 2, paint1);
        super.onDraw(canvas);
    }

    //绘制盒子的盖子
    private void drawCover(Canvas canvas) {
        //绘制左边的盖子
        leftPath.moveTo(lCCPointInner.pointX, lCCPointInner.pointY);
        leftPath.lineTo(lCCPointOut.pointX, lCCPointOut.pointY);
        leftPath.lineTo(pointLeftOut.pointX, pointLeftOut.pointY);
        leftPath.lineTo(pointLeftInner.pointX, pointLeftInner.pointY);
        leftPath.close();
        //绘制右边的盖子
        rightPath.moveTo(rCCPointInner.pointX, rCCPointInner.pointY);
        rightPath.lineTo(rCCPointOut.pointX, rCCPointOut.pointY);
        rightPath.lineTo(pointRightOut.pointX, pointRightOut.pointY);
        rightPath.lineTo(pointRightInner.pointX, pointRightInner.pointY);
        rightPath.close();
        paint1.setColor(Color.parseColor("#FFCDCDCE"));
        canvas.drawPath(leftPath, paint1);
        canvas.drawPath(rightPath, paint1);
        //path需要重置  不然会有之前的绘制图像
        leftPath.reset();
        rightPath.reset();
    }

    //绘制标语
    //// TODO: 17/8/17 关于每个字的位置 这里简单写了一下 需要做调整
    private void drawSlogan(Canvas canvas) {
        if (viewStatus == ViewStatus.START_SLOGAN || viewStatus == ViewStatus.START_SHAKE) {
            float x;
            float y;
            for (int i = 0; i < slogan.length; i++) {
                if (viewStatus == ViewStatus.START_SLOGAN) {
                    //好的生活
                    if (i <= 3) {
                        x = lCCPointOut.pointX + 100 - slogan[i].getWidth() - jumpInt * (2 - i) / 4;
                        y = lCCPointOut.pointY - jumpInt * 5 / 7;
                    } else {//没那么贵
                        x = lCCPointOut.pointX + 100 + jumpInt * (i - 5) / 4;
                        y = lCCPointOut.pointY - jumpInt * 2 / 5;
                    }
                    points[i].setPointX(x);
                    points[i].setPointY(y);
                    canvas.drawBitmap(slogan[i], points[i].pointX, points[i].pointY, paint1);
                } else {
                    canvas.drawBitmap(slogan[i], points[i].pointX, points[i].pointY + (i % 2 == 0 ? shakeInt : -shakeInt), paint1);
                }
            }
        }
    }


    //文字重盒子中弹起的动画
    public void startJumpAnim() {
        viewStatus = ViewStatus.START_SLOGAN;
        ValueAnimator animator = ValueAnimator.ofInt(0, 200);
        animator.setDuration(700);
        animator.setInterpolator(new DecelerateInterpolator(2f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                jumpInt = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewStatus = ViewStatus.START_SHAKE;
                //开始抖动文字
                shakeSlogan();
            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    //标语抖动动画
    public void shakeSlogan() {
        //简单实现的抖动动画 想要实现更好的效果可以查看https://github.com/hujiaweibujidao/wava
        ValueAnimator animator = ValueAnimator.ofInt(-2, 2);
        animator.setRepeatCount(-1);
        animator.setDuration(200);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                shakeInt = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    //设置偏移 这里为了方便计算 设置210偏移对应盖子打开的最大角度 ＋－210
    public void setDistance(int distance) {
        this.distance = distance;
        int animOffset = distance - viewSizeHeight/2-30;
        if (viewStatus == ViewStatus.START && animOffset > 0) {
            if (leftAngle >= -210) {
                //规避原则  防止下拉速度过快 distance并不是以+1的形式递增  而是一下子加很多 造成画图不准
                if (animOffset >= 210) {
                    animOffset = 210;
                }
                leftAngle = -animOffset;
                rightAngle = Math.abs(leftAngle) - 180;
                invalidate();
            }
        }else if(viewStatus == ViewStatus.START&&animOffset<=0){
            restoreView();
        }
    }

    //设置是否正在刷新
    public void setViewStatus(ViewStatus viewStatus) {
        this.viewStatus = viewStatus;
        if (viewStatus == ViewStatus.REFRESHING) {
            //可能下拉没有到210  就开始刷新了 看具体xml中的height  // TODO: 17/8/18 需要调整完善的地方
            leftAngle = -210;
            rightAngle = 30;
            invalidate();
            startJumpAnim();
        }
    }

    //刷新完毕 重置view的状态
    public void restoreView() {
        viewStatus = ViewStatus.START;
        leftAngle = 0;
        rightAngle = -180;
        distance = 0;
        invalidate();
    }

}
