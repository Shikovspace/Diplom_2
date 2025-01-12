package user;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class UserDataUpdateTest {
    private String userAccessToken;

    User user = new User();

    @Before
    public void tearUp() throws Exception {
        // Создание пользователя
        user = User.getRandomUser();
        // Создание учетной записи пользователя
        userAccessToken = UserSpec.getResponseCreateUser(user,200).accessToken;
    }

    // Удаление учетной записи пользователя
    @After
    public void tearDown() throws Exception {
        UserSpec.getResponseUserDeleted(userAccessToken, 202);
    }

    @Test
    @DisplayName("Тест успешного изменения данных (пароля) авторизованного пользователя")
    public void changePasswordOfTheAuthorizationUserTestOk() throws JsonProcessingException {
        User createdUser = new User(user.getEmail(), user.getPassword());
        // Авторизация пользователя
        UserSpec.getResponseUserAuthorization(createdUser, 200);
        // Изменение пароля пользователя
        String updatedPassword = "New" + user.getPassword();
        User updatedUser = new User(user.getEmail(), updatedPassword, user.getName());
        // Изменение данных пользователя
        UserSpec.getResponseUpdateUserData(updatedUser, userAccessToken, 200);
        // Авторизация с измененным паролем
        userAccessToken = UserSpec.getResponseUserAuthorization(updatedUser, 200).accessToken;
        assertThat(userAccessToken, notNullValue());
    }

    @Test
    @DisplayName("Тест успешного изменения данных (имени) авторизованного пользователя")
    public void successfullChangeNameOfTheAuthorizationUserTestOk() throws JsonProcessingException {
        User createdUser = new User(user.getEmail(), user.getPassword());
        // Авторизация пользователя
        userAccessToken = UserSpec.getResponseUserAuthorization(createdUser, 200).accessToken;
        // Изменение имени пользователя
        String updatedName = "New" + user.getName();
        User updatedUser = new User(user.getEmail(), user.getPassword(), updatedName);
        // Изменение данных пользователя
        UserSpec.getResponseUpdateUserData(updatedUser, userAccessToken, 200)
                .body("user.name",equalTo(updatedName));
    }

    @Test
    @DisplayName("Тест успешного изменения данных (email) авторизованного пользователя")
    public void successfullChangeEmailOfTheAuthorizationUserTestOk() throws JsonProcessingException {
        User createdUser = new User(user.getEmail(), user.getPassword());
        // Авторизация пользователя
        userAccessToken = UserSpec.getResponseUserAuthorization(createdUser, 200).accessToken;
        // Изменение email пользователя
        String updatedEmail = "New" + user.getEmail();
        User updatedUser = new User(updatedEmail, user.getPassword(), user.getName());
        // Изменение данных пользователя
        UserSpec.getResponseUpdateUserData(updatedUser, userAccessToken, 200)
                .body("user.email",equalTo(updatedEmail.toLowerCase()));
    }

    @Test
    @DisplayName("Тест неуспешного изменения данных (пароля) неавторизованного пользователя")
    public void failChangePasswordOfTheUnauthorizationUserTestOk() throws JsonProcessingException {
        // Изменение пароля пользователя
        String updatedPassword = "New" + user.getPassword();
        User updatedUser = new User(user.getEmail(), updatedPassword, user.getName());
        // Изменение данных пользователя
        UserSpec.getResponseUpdateUserData(updatedUser, "", 401)
                .body("message",equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Тест неуспешного изменения данных (имени) неавторизованного пользователя")
    public void failChangeNameOfTheUnauthorizationUserTestOk() throws JsonProcessingException {
        // Изменение имени пользователя
        String updatedName = "New" + user.getName();
        User updatedUser = new User(user.getEmail(), user.getPassword(), updatedName);
        // Изменение данных пользователя
        UserSpec.getResponseUpdateUserData(updatedUser, "", 401)
                .body("message",equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Тест неуспешного изменения данных (email) неавторизованного пользователя")
    public void failChangeEmailOfTheUnauthorizationUserTestOk() throws JsonProcessingException {
        // Изменение email пользователя
        String updatedEmail = "New" + user.getEmail();
        User updatedUser = new User(updatedEmail, user.getPassword(), user.getName());
        // Изменение данных пользователя
        UserSpec.getResponseUpdateUserData(updatedUser, "", 401)
                .body("message",equalTo("You should be authorised"));
    }
}
