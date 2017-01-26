package james.buttons;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
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

    private Drawable rippleDrawable;
    private int rippleX, rippleY;
    private boolean isRippleEnabled = true;

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
        paint.setAlpha(100);

        setBackgroundColor(color);
        setBackgroundType(type);

        setOnTouchListener(this);
    }

    public void setBackgroundType(Type type) {
        setBackgroundType(type, true);
    }

    public void setBackgroundType(Type type, boolean autoTextContrast) {
        this.type = type;

        int resource = R.drawable.button;
        Integer rippleResource = null;

        switch (type) {
            case SOLID:
                resource = R.drawable.button;
                break;
            case OUTLINE:
                resource = R.drawable.button_outline;
                rippleResource = R.drawable.button;
                break;
            case ROUND:
                resource = R.drawable.button_round;
                break;
            case ROUND_OUTLINE:
                resource = R.drawable.button_outline_round;
                rippleResource = R.drawable.button_round;
                break;
        }

        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), resource));
        if (rippleResource != null)
            rippleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), rippleResource));
        else rippleDrawable = null;

        setBackgroundDrawable(drawable);

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
        if (rippleAnimator == null || !rippleAnimator.isRunning()) {
            paint.setColor(color);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isRippleEnabled && progress > 0) {
            if (rippleX == 0 && rippleY == 0) {
                rippleX = canvas.getWidth() / 2;
                rippleY = canvas.getHeight() / 2;
            }

            Bitmap ripple = getRipple();
            if (ripple != null)
                canvas.drawBitmap(ripple, 0, 0, paint);
        }
    }

    @Nullable
    private Bitmap getRipple() {
        if (getBackground() == null || getWidth() < 1 || getHeight() < 1)
            return null;

        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            return null;
        }

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.paint.getColor());
        paint.setAlpha(255);

        if (rippleDrawable != null) rippleDrawable.draw(canvas);
        else getBackground().draw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawCircle(rippleX, rippleY, progress * (getWidth() / 1.5f), paint);

        return bitmap;
    }

    public void setRippleEnabled(boolean isRippleEnabled) {
        this.isRippleEnabled = isRippleEnabled;
        if (rippleAnimator != null && rippleAnimator.isRunning())
            rippleAnimator.cancel();

        progress = 0;
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isRippleEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (rippleAnimator != null)
                        rippleAnimator.cancel();

                    rippleX = (int) event.getX();
                    rippleY = (int) event.getY();

                    rippleAnimator = ValueAnimator.ofFloat(0, 1).setDuration(2500);
                    rippleAnimator.setInterpolator(new DecelerateInterpolator());
                    rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            progress = (float) animation.getAnimatedValue();
                            paint.setAlpha(Math.min((int) (255 * animation.getAnimatedFraction()), 100));
                            invalidate();
                        }
                    });
                    rippleAnimator.start();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (rippleAnimator != null)
                        rippleAnimator.cancel();

                    rippleAnimator = ValueAnimator.ofFloat(progress, 1).setDuration(350);
                    rippleAnimator.setInterpolator(new AccelerateInterpolator());
                    rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            progress = (float) animation.getAnimatedValue();
                            paint.setAlpha((int) (100 * (1 - animation.getAnimatedFraction())));
                            invalidate();
                        }
                    });
                    rippleAnimator.start();
                    break;
            }
        }
        return false;
    }

    public enum Type {
        SOLID,
        OUTLINE,
        ROUND,
        ROUND_OUTLINE
    }

    private class Position {

        private float progress;
        private int x, y;

        private Position(float progress, int x, int y) {
            this.progress = progress;
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) || (obj instanceof Position && ((Position) obj).progress == progress && ((Position) obj).x == x && ((Position) obj).y == y);
        }
    }
}
