package james.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import james.buttons.utils.ColorUtils;

public class Button extends AppCompatButton {

    @ColorInt
    private int color;

    private Type type;

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

        setBackgroundColor(color);
        setBackgroundType(type);
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

    public enum Type {
        SOLID,
        OUTLINE,
        ROUND,
        ROUND_OUTLINE
    }
}
