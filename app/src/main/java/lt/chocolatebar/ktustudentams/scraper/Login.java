package lt.chocolatebar.ktustudentams.scraper;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Login extends AsyncTask<String, Void, Boolean> {

    private final static String UAIS_LOGIN_URL = "https://uais.cr.ktu.lt/ktuis/studautologin";
    private final static String LOGIN_FORM_POST_URL = "https://login.ktu.lt/simplesaml/module.php/core/loginuserpass.php";

    private OnLoginFinishedListener onLoginFinishedListener;

    public void loginAsync(String username, String password) {
        execute(username, password);
    }

    public void setOnLoginFinishedListener(OnLoginFinishedListener onLoginFinishedListener) {
        this.onLoginFinishedListener = onLoginFinishedListener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            String username = params[0];
            String password = params[1];
            return login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean login(String username, String password) throws IOException {
        Document loginPage = openLoginPage();
        String authState = loginPage.select("#content > form > input[type=\"hidden\"]").get(0).attr("value");
        Document loginFormResponse = postLoginForm(username, password, authState);
        if (hasUsernameNode(loginFormResponse, username)) {
            return true;
        } else if (hasErrorNode(loginFormResponse)) {
            return false;
        }
        return null;
    }

    private Document openLoginPage() throws IOException {
        Connection.Response response = Jsoup.connect(UAIS_LOGIN_URL).method(Connection.Method.GET).execute();
        Cookies.add(response.cookies());
        return response.parse();
    }

    private Document postLoginForm(String username, String password, String authState) throws IOException {
        return Jsoup.connect(LOGIN_FORM_POST_URL)
                .cookies(Cookies.get())
                .data("AuthState", authState)
                .data("username", username)
                .data("password", password)
                .post();
    }

    private boolean hasUsernameNode(Document loginFormResponse, String username) {
        return loginFormResponse.select("#table_with_attributes > tbody > tr:nth-child(7) > td > div").html().equals(username);
    }

    private boolean hasErrorNode(Document loginFormResponse) {
        String errorText = loginFormResponse.select("#content > div > h2").html();
        return errorText.equals("Error") || errorText.equals("Klaida");
    }

    @Override
    protected void onPostExecute(Boolean isLoggedIn) {
        super.onPostExecute(isLoggedIn);
        if (isLoggedIn != null) {
            onLoginFinishedListener.onLoginFinished(isLoggedIn);
        } else {
            onLoginFinishedListener.onLoginFailure();
        }
    }

    public interface OnLoginFinishedListener {
        void onLoginFinished(boolean isLoggedIn);
        void onLoginFailure();
    }
}
