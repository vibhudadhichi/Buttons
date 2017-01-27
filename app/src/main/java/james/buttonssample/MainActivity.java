package james.buttonssample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import james.buttons.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        button.setOnClickListener(this);
        outlineButton.setOnClickListener(this);
        roundButton.setOnClickListener(this);
        roundOutlineButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, R.string.pie, Toast.LENGTH_SHORT).show();
    }
}
