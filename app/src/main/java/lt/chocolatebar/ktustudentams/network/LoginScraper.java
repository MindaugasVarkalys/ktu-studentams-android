package lt.chocolatebar.ktustudentams.network;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import lt.chocolatebar.ktustudentams.data.Cookies;
import lt.chocolatebar.ktustudentams.data.User;

public class LoginScraper extends AsyncTask<Void, Void, Boolean> {

    private final static String UAIS_LOGIN_URL = "https://uais.cr.ktu.lt/ktuis/studautologin";
    private final static String LOGIN_FORM_POST_URL = "https://login.ktu.lt/simplesaml/module.php/core/loginuserpass.php";
    private final static String LOGIN_ACCEPT_POST_URL = "https://login.ktu.lt/simplesaml/module.php/consent/getconsent.php";
    private final static String UNSUPPORTED_JAVASCRIPT_POST_URL = "https://uais.cr.ktu.lt/shibboleth/SAML2/POST";

    private OnLoginFinishedListener onLoginFinishedListener;
    private User user;

    public void loginAsync(String username, String password) {
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        execute();
    }

    public void setOnLoginFinishedListener(OnLoginFinishedListener onLoginFinishedListener) {
        this.onLoginFinishedListener = onLoginFinishedListener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return login();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean login() throws Exception {
        Document loginPage = openLoginPage();
        String authState = loginPage.select("#content > form > input[name=\"AuthState\"]").attr("value");
        Document loginFormResponse = postLoginForm(authState);
        if (hasErrorNode(loginFormResponse)) {
            return false;
        }
        user.setEmail(parseUserEmailFromLoginFormResponse(loginFormResponse));
        Document formResponse = submitAcceptForm(loginFormResponse);
        Document uais = submitUnsupportedJavaScriptForm(formResponse);
        parseUserData(uais);
        return true;
    }

    private Document openLoginPage() throws IOException {
        Connection.Response response = Jsoup.connect(UAIS_LOGIN_URL)
                .method(Connection.Method.GET)
                .execute();
        Cookies.add(response.cookies());
        return response.parse();
    }

    private Document postLoginForm(String authState) throws IOException {
        Connection.Response response = Jsoup.connect(LOGIN_FORM_POST_URL)
                .method(Connection.Method.POST)
                .cookies(Cookies.get())
                .data("AuthState", authState)
                .data("username", user.getUsername())
                .data("password", user.getPassword())
                .execute();
        Cookies.add(response.cookies());
        return response.parse();
    }

    private boolean hasErrorNode(Document loginFormResponse) {
        String errorText = loginFormResponse.select("#content > div > h2").html();
        return errorText.equals("Error") || errorText.equals("Klaida");
    }

    private String parseUserEmailFromLoginFormResponse(Document loginFormResponse) {
        return loginFormResponse.select("#table_with_attributes > tbody > tr:nth-child(1) > td > div").html();
    }

    private Document submitAcceptForm(Document loginFormResponse) throws IOException {
        String stateId = loginFormResponse.select("#content > form:nth-child(2) > p > input[name=\"StateId\"]").attr("value");
        Connection.Response response = Jsoup.connect(LOGIN_ACCEPT_POST_URL)
                .method(Connection.Method.POST)
                .cookies(Cookies.get())
                .data("saveconsent", "1")
                .data("StateId", stateId)
                .data("yes", "")
                .execute();
        Cookies.add(response.cookies());
        return response.parse();
    }

    private Document submitUnsupportedJavaScriptForm(Document javaScriptUnsupportedPage) throws IOException {
        String SAMLResponse = javaScriptUnsupportedPage.select("body > form > input[name=\"SAMLResponse\"]").attr("value");
        String relayState = javaScriptUnsupportedPage.select("body > form > input[name=\"RelayState\"]").attr("value");
        Connection.Response response = Jsoup.connect(UNSUPPORTED_JAVASCRIPT_POST_URL)
                .method(Connection.Method.POST)
                .cookies(Cookies.get())
                .data("SAMLResponse", SAMLResponse)
                .data("RelayState", relayState)
                .execute();
        Cookies.add(response.cookies());
        return response.parse();
    }

    private void parseUserData(Document uais) {
        String html = uais.select("body > div.navbar > div > div > div > p").html();
        String nameWithCode = html.substring(0, html.indexOf('<'));
        String[] splitNameWithCode = nameWithCode.split(" ");
        user.setCode(splitNameWithCode[0]);
        user.setSurname(splitNameWithCode[1]);
        user.setName(splitNameWithCode[2]);
    }

    @Override
    protected void onPostExecute(Boolean isLoggedIn) {
        super.onPostExecute(isLoggedIn);
        if (isLoggedIn == null) {
            onLoginFinishedListener.onLoginError();
        } else if (isLoggedIn) {
            onLoginFinishedListener.onLoginSuccess(user);
        } else {
            onLoginFinishedListener.onIncorrectCredentials();
        }
    }

    public interface OnLoginFinishedListener {
        void onLoginSuccess(User user);
        void onIncorrectCredentials();
        void onLoginError();
    }
}
