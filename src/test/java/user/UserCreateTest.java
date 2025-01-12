package user;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class UserCreateTest {
    private String userAccessToken;
    private boolean userCreateSuccess;

    User user = new User();

    // Удаление учетной записи пользователя
    @After
    public void tearDown() throws Exception {
        if (userCreateSuccess) {
            UserSpec.getResponseUserDeleted(userAccessToken, 202);
        }
    }

    @Test
    @DisplayName("Тест успешного создания учетной записи пользователя")
    public void successfulCreateUserTestOk() throws JsonProcessingException {
        // Создание пользователя
        user = User.getRandomUser();
        // Создание учетки пользователя
        UserSpec response = UserSpec.getResponseCreateUser(user,200);
        userAccessToken = response.accessToken;
        userCreateSuccess = response.success;
        assertThat(userAccessToken, notNullValue());
        assertTrue(userCreateSuccess);
    }

    @Test
    @DisplayName("Тест неуспешного создания учетной записи пользователя без пароля")
    public void failCreateUserWithOutPasswordTestOk() throws JsonProcessingException {
        // Создание пользователя без пароля
        user = User.getRandomUserWithoutPassword();
        // Создание учетки пользователя
        UserSpec response = UserSpec.getResponseCreateUser(user, 403);
        assertFalse(response.success);
        assertEquals("Email, password and name are required fields",response.message);
    }

    @Test
    @DisplayName("Тест неуспешного создания учетной записи пользователя без имени")
    public void failCreateUserWithOutNameTestOk() throws JsonProcessingException {
        // Создание пользователя без имени
        user = User.getRandomUserWithoutName();
        // Создание "учетки" пользователя
        UserSpec response = UserSpec.getResponseCreateUser(user, 403);
        assertFalse(response.success);
        assertEquals("Email, password and name are required fields",response.message);
    }

    @Test
    @DisplayName("Тест неуспешного создания учетной записи пользователя без email")
    public void failCreateUserWithOutEmailTestOk() throws JsonProcessingException {
        // Создание пользователя без email
        user = User.getRandomUserWithoutEmail();
        // Создание учетки пользователя
        UserSpec response = UserSpec.getResponseCreateUser(user, 403);
        assertFalse(response.success);
        assertEquals("Email, password and name are required fields",response.message);
    }

    @Test
    @DisplayName("Тест неуспешного создания учетной записи " +
            "пользователя который уже зарегистрирован (с повторяющимся email)")
    public void failCreateCourierRecurringEmailTestOk() throws JsonProcessingException {
        // Создание пользователя
        user = User.getRandomUser();
        // Создание учетки пользователя
        UserSpec initResponse = UserSpec.getResponseCreateUser(user,200);
        userAccessToken = initResponse.accessToken;
        userCreateSuccess = initResponse.success;
        // Создание учетки пользователя который уже зарегистрирован
        UserSpec response = UserSpec.getResponseCreateUser(user, 403);
        assertFalse(response.success);
        assertEquals("User already exists",response.message);
    }
}
