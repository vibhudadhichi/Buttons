package james.buttonssample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import james.buttons.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button outlineButton;
    private Button roundButton;
    private Button roundOutlineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        outlineButton = (Button) findViewById(R.id.outlineButton);
        roundButton = (Button) findViewById(R.id.roundButton);
        roundOutlineButton = (Button) findViewById(R.id.roundOutlineButton);
    }
}
