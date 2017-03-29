package lt.chocolatebar.ktustudentams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this::onLoginButtonClick);
        passwordInput.setOnEditorActionListener(this::onEditorAction);
    }

    protected boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_GO){
            onLoginButtonClick(v);
            return true;
        }
        return false;
    }

    protected void onLoginButtonClick(View v) {
        if (usernameInput.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.username_error_message, Toast.LENGTH_SHORT).show();
            usernameInput.requestFocus();
        } else if (passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.password_error_message, Toast.LENGTH_SHORT).show();
            passwordInput.requestFocus();
        } else {
            startActivity(new Intent(this, GradesActivity.class));
            finish();
        }
    }
}
