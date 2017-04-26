package lt.chocolatebar.ktustudentams.data;

import java.util.HashMap;
import java.util.Map;

public class Cookies {

    private static Map<String, String> cookies = new HashMap<>();

    public static void add(Map<String, String> cookies) {
        Cookies.cookies.putAll(cookies);
    }

    public static Map<String, String> get() {
        return cookies;
    }

}
