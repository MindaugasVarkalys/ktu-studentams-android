package lt.chocolatebar.ktustudentams;

import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static EditText usernameInput;
    private static EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
        final Button button = (Button) findViewById(R.id.login);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonClicked(v);
                    }
                }
        );
    }

    protected void buttonClicked(View v){
        if(TextUtils.isEmpty(usernameInput.getText().toString()) ||
                TextUtils.isEmpty(passwordInput.getText().toString())) {
            Toast.makeText(this, "Netinkami prisijungimo duomenys", Toast.LENGTH_SHORT).show();
        }
    }
}
