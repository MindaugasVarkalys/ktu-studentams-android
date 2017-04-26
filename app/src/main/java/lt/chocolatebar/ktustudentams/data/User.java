package lt.chocolatebar.ktustudentams.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class User {

    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private String code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<String> toStringSet() {
        Set<String> set = new HashSet<>();
        set.add(username);
        set.add(password);
        set.add(email);
        set.add(name);
        set.add(surname);
        set.add(code);
        return set;
    }

    public static User getFromStringSet(Set<String> set) {
        Iterator<String> iterator = set.iterator();
        User user = new User();
        user.username = iterator.next();
        user.password = iterator.next();
        user.email = iterator.next();
        user.name = iterator.next();
        user.surname = iterator.next();
        user.code = iterator.next();
        return user;
    }
}
