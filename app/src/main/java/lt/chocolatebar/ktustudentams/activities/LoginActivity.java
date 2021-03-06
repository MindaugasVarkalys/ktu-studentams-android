package lt.chocolatebar.ktustudentams.activities;

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

import lt.chocolatebar.ktustudentams.LoadingDialog;
import lt.chocolatebar.ktustudentams.R;
import lt.chocolatebar.ktustudentams.SharedPrefs;
import lt.chocolatebar.ktustudentams.data.User;
import lt.chocolatebar.ktustudentams.network.LoginScraper;
import lt.chocolatebar.ktustudentams.network.NetworkUtils;

public class LoginActivity extends AppCompatActivity implements LoginScraper.OnLoginFinishedListener {

    private EditText usernameInput;
    private EditText passwordInput;

    private SharedPrefs sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefs = new SharedPrefs(this);
        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this::onLoginButtonClick);
        passwordInput.setOnEditorActionListener(this::onEditorAction);

        if (sharedPrefs.hasUser()) {
            User user = sharedPrefs.getUser();
            usernameInput.setText(user.getUsername());
            passwordInput.setText(user.getPassword());
            if (NetworkUtils.isNetworkAvailable(this)) {
                login(user.getUsername(), user.getPassword());
            } else Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();

        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            onLoginButtonClick(v);
            return true;
        }
        return false;
    }

    public void onLoginButtonClick(View v) {
        if (usernameInput.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_username, Toast.LENGTH_SHORT).show();
            usernameInput.requestFocus();
        } else if (passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
            passwordInput.requestFocus();
        } else if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            String username = usernameInput.getText().toString().toLowerCase();
            String password = passwordInput.getText().toString();
            login(username, password);
        }
    }

    private void login(String username, String password) {
        LoadingDialog.show(this);
        LoginScraper login = new LoginScraper();
        login.setOnLoginFinishedListener(this);
        login.loginAsync(username, password);
    }

    @Override
    public void onLoginSuccess(User user) {
        LoadingDialog.dismiss();
        sharedPrefs.saveUser(user);
        startActivity(new Intent(this, SideMenuActivity.class));
        finish();
    }

    @Override
    public void onIncorrectCredentials() {
        LoadingDialog.dismiss();
        Toast.makeText(this, R.string.incorrect_username_or_password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginError() {
        LoadingDialog.dismiss();
        Toast.makeText(this, R.string.failure, Toast.LENGTH_SHORT).show();
    }
}
