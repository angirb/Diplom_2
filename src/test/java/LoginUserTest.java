import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;


public class LoginUserTest{
    private User user;
    private UserCredentials userCredentials;
    private UserApi userApi;
    private ValidatableResponse validatableResponse;
    String bearerToken;


    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }
    @Before
    public void setUp() {
        userApi = new UserApi();
        user = new User("raikiri@yandex.ru", "konoha123", "Kakashi");
    }
//    @After
//    public void cleanData() {
//        if (validatableResponse != null) {
//            String bearerToken = validatableResponse.extract().path("accessToken");
//            userApi.delete(bearerToken);
//        }
//    }
    @After
    public void cleanData() {
        userApi.delete(bearerToken);
    }
    @Test
    @DisplayName("loging with created account")
    @Description("creating account in advance, checking if login is successful")
    public void loginUserWithCreatedAccount() {
        UserApi userApi = new UserApi();
        ValidatableResponse response = new UserApi().login(user);
        int statusCode = response.extract().statusCode();
        assertEquals("Successful login", HTTP_OK, statusCode);
    }

    @Test
    @DisplayName("login with invalid email")
    @Description("trying to login with invalid user information")
    public void loginUserWithInvalidUserAccount() {
        User user = new User("invalid@yandex.ru", "konoha123", "Kakashi");
        ValidatableResponse loginResponse = new UserApi().login(user);
        int statusCode = loginResponse.extract().statusCode();

        assertEquals("Invalid login information", HTTP_UNAUTHORIZED, statusCode);
    }
    @Test
    @DisplayName("login with invalid password")
    @Description("trying to login with invalid user password")
    public void loginUserWithInvalidUserPassword() {
        User user = new User("raikiri@yandex.ru", "konoha123123123123", "Kakashi");
        ValidatableResponse loginResponse = new UserApi().login(user);
        int statusCode = loginResponse.extract().statusCode();

        assertEquals("Invalid login information", HTTP_UNAUTHORIZED, statusCode);
    }
}
