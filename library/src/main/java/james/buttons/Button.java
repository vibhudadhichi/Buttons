package james.buttons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import james.buttons.utils.ColorUtils;

public class Button extends AppCompatButton implements View.OnTouchListener {

    @ColorInt
    private int color;
    private float progress;

    private Type type;
    private Paint paint;
    private ValueAnimator rippleAnimator;

    public Button(Context context) {
        super(context);
        init(null);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Button, 0, 0);

            switch (array.getInteger(R.styleable.Button_backgroundType, 0)) {
                case 0:
                    type = Type.SOLID;
                    break;
                case 1:
                    type = Type.OUTLINE;
                    break;
                case 2:
                    type = Type.ROUND;
                    break;
                case 3:
                    type = Type.ROUND_OUTLINE;
                    break;
            }

            color = array.getColor(R.styleable.Button_backgroundColor, Color.BLACK);
        } else {
            type = Type.SOLID;
            color = Color.BLACK;
        }

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setAlpha(150);

        setBackgroundColor(color);
        setBackgroundType(type);

        setOnTouchListener(this);
    }

    public void setBackgroundType(Type type) {
        setBackgroundType(type, true);
    }

    public void setBackgroundType(Type type, boolean autoTextContrast) {
        this.type = type;

        switch (type) {
            case SOLID:
                setBackgroundDrawable(DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.button)));
                break;
            case OUTLINE:
                setBackgroundDrawable(DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.button_outline)));
                break;
            case ROUND:
                setBackgroundDrawable(DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.button_round)));
                break;
            case ROUND_OUTLINE:
                setBackgroundDrawable(DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.button_outline_round)));
                break;
        }

        if (autoTextContrast)
            setBackgroundColor(color);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        setBackgroundColor(color, true);
    }

    public void setBackgroundColor(@ColorInt int color, boolean autoTextContrast) {
        this.color = color;

        DrawableCompat.setTint(getBackground(), color);

        if (autoTextContrast) {
            switch (type) {
                case SOLID:
                case ROUND:
                    setTextColor(ColorUtils.isColorDark(color) ? Color.WHITE : Color.BLACK);
                    break;
                case OUTLINE:
                case ROUND_OUTLINE:
                    setTextColor(color);
                    break;
            }
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (rippleAnimator == null || !rippleAnimator.isRunning())
            paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, progress * (canvas.getWidth() / 1.5f), paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rippleAnimator != null)
                    rippleAnimator.cancel();

                rippleAnimator = ValueAnimator.ofFloat(0, 1);
                rippleAnimator.setDuration(2500);
                rippleAnimator.setInterpolator(new DecelerateInterpolator());
                rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        progress = (float) animation.getAnimatedValue();
                        paint.setAlpha(150);
                        invalidate();
                    }
                });
                rippleAnimator.start();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (rippleAnimator != null)
                    rippleAnimator.cancel();

                rippleAnimator = ValueAnimator.ofFloat(progress, 1);
                rippleAnimator.setDuration(350);
                rippleAnimator.setInterpolator(new AccelerateInterpolator());
                rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        progress = (float) animation.getAnimatedValue();
                        paint.setAlpha((int) (150 * (1 - animation.getAnimatedFraction())));
                        invalidate();
                    }
                });
                rippleAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progress = 0;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                rippleAnimator.start();

                break;
        }
        return false;
    }

    public enum Type {
        SOLID,
        OUTLINE,
        ROUND,
        ROUND_OUTLINE
    }
}
