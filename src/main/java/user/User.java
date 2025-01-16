package user;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String email;
    private String password;
    private String name;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Создание пользователя
    public static User getRandomUser() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@example.com",
                "123456",
                "TestUser_" + RandomStringUtils.randomAlphanumeric(6)
        );
    }

    // Создание пользователя без пароля
    public static User getRandomUserWithoutPassword() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@example.com",
                "",
                "TestUser_" + RandomStringUtils.randomAlphanumeric(6)
        );
    }

    // Создание пользователя без имени
    public static User getRandomUserWithoutName() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@example.com",
                "123456",
                ""
        );
    }

    // Создание пользователя без email
    public static User getRandomUserWithoutEmail() {
        return new User(
                "",
                "123456",
                "TestUser_" + RandomStringUtils.randomAlphanumeric(6)
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
