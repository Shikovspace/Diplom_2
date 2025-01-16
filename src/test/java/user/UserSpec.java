package user;

import config.Config;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserSpec {
    private final String ROOT = "/auth/register";
    private final String LOGIN = "/auth/login";
    private final String LOGOUT = "/auth/logout";
    private String UPDATE= "/auth/user";
    private String jsonString;
    public String message;
    public boolean success;
    public String accessToken;
    public String refreshToken;

    public UserSpec () {}

    public UserSpec (boolean success, String message, String accessToken, String refreshToken) {
        this.success = success;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    static ObjectMapper mapper = new ObjectMapper();

    @Step("Создание учетной записи пользователя")
    public UserSpec getResponseCreateUser(User user, int statusCode) throws JsonProcessingException {
        jsonString = mapper.writeValueAsString(user);
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(jsonString)
                .when()
                .post(ROOT)
                .then().log().all()
                .statusCode(statusCode)
                .extract()
                .response();
        success = response.path("success");
        message = response.path("message");
        accessToken = response.path("accessToken");
        refreshToken = response.path("refreshToken");
        return new UserSpec(success, message, accessToken, refreshToken);
    }

    @Step("Авторизация учетной записи пользователя")
    public UserSpec getResponseUserAuthorization (User user, int statusCode) throws JsonProcessingException {
        jsonString = mapper.writeValueAsString(user);
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(jsonString)
                .when()
                .post(LOGIN)
                .then().log().all()
                .statusCode(statusCode).extract()
                .response();
        success = response.path("success");
        message = response.path("message");
        accessToken = response.path("accessToken");
        refreshToken = response.path("refreshToken");
        return new UserSpec(success, message, accessToken, refreshToken);
    }

    @Step("Выход из учетной записи пользователя")
    public ValidatableResponse getResponseLogoutUser(String userRefreshToken, int statusCode) {
        jsonString = "{\"token\": \"" + userRefreshToken + "\"}";
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(jsonString)
                .when()
                .post(LOGOUT)
                .then().log().all()
                .statusCode(statusCode);
    }

    @Step("Удаление пользователя")
    public ValidatableResponse getResponseUserDeleted(String userAccessToken, int statusCode) {
        return given().log().all()
                .header("Authorization", userAccessToken)
                .baseUri(Config.BASE_URL)
                .when()
                .delete(UPDATE)
                .then().log().all()
                .statusCode(statusCode);
    }

    @Step("Обновление данных пользователя")
    public ValidatableResponse getResponseUpdateUserData(User user,
                                                                String userAccessToken,
                                                                int statusCode) throws JsonProcessingException {
        jsonString = mapper.writeValueAsString(user);
        return given().log().all()
                .headers("Authorization", userAccessToken, "Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(jsonString)
                .when()
                .patch(UPDATE)
                .then().log().all()
                .statusCode(statusCode);
    }
}
