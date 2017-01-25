package james.buttonssample;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;

import java.util.Random;

import james.buttons.Button;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private Random random;

    private Button button;
    private Button outlineButton;
    private Button roundButton;
    private Button roundOutlineButton;

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        outlineButton = (Button) findViewById(R.id.outlineButton);
        roundButton = (Button) findViewById(R.id.roundButton);
        roundOutlineButton = (Button) findViewById(R.id.roundOutlineButton);

        random = new Random();
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int color = Color.rgb((int) (random.nextDouble() * 150), (int) (random.nextDouble() * 150), (int) (random.nextDouble() * 150));
                ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), MainActivity.this.color, color);
                animator.setDuration(500);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int color = (int) animation.getAnimatedValue();
                        button.setBackgroundColor(color);
                        outlineButton.setBackgroundColor(color);
                        roundButton.setBackgroundColor(color);
                        roundOutlineButton.setBackgroundColor(color);
                    }
                });
                animator.start();

                MainActivity.this.color = color;

                handler.postDelayed(this, 500);
            }
        });
    }
}
