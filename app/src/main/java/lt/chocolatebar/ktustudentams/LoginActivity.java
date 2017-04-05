package lt.chocolatebar.ktustudentams;

import android.app.ProgressDialog;
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

import lt.chocolatebar.ktustudentams.scraper.Login;
import lt.chocolatebar.ktustudentams.scraper.NetworkUtils;

public class LoginActivity extends AppCompatActivity implements Login.OnLoginFinishedListener {

    private EditText usernameInput;
    private EditText passwordInput;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this::onLoginButtonClick);
        passwordInput.setOnEditorActionListener(this::onEditorAction);
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            onLoginButtonClick(v);
            return true;
        }
        return false;
    }

    public void onLoginButtonClick(View v) {
        /*
        // Uncomment these lines to skip login
        startActivity(new Intent(this, SideMenuActivity.class));
        finish();
        */
        if (usernameInput.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_username, Toast.LENGTH_SHORT).show();
            usernameInput.requestFocus();
        } else if (passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
            passwordInput.requestFocus();
        } else if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.please_wait));
            Login login = new Login();
            login.setOnLoginFinishedListener(this);
            login.loginAsync(usernameInput.getText().toString().toLowerCase(), passwordInput.getText().toString());
        }
    }

    @Override
    public void onLoginFinished(boolean isLoggedIn) {
        progressDialog.dismiss();
        if (isLoggedIn) {
            startActivity(new Intent(this, SideMenuActivity.class));
            finish();
        } else {
            Toast.makeText(this, R.string.incorrect_username_or_password, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginFailure() {
        progressDialog.dismiss();
        Toast.makeText(this, R.string.login_failure, Toast.LENGTH_SHORT).show();
    }
}
