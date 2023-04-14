import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class UpdateUserTest extends UserApi {
    private UserApi userApi;
    private User user;
    String bearerToken;



    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        userApi = new UserApi();
        user = UserGenerator.getRandom();
        userApi.create(user);
    }
    @After
    public void cleanData() {
        if (bearerToken != null) {
            userApi.delete(bearerToken);
        }
    }

    private String generateRandomEmail() {
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        return generatedString + "@yandex.ru";

    }
    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }


    @Test
    @DisplayName("change authorized user email and password ")
    @Description("testing user modifying information as email and password")
    public void changeUserEmailPasswordWithAuth() {
        ValidatableResponse response = userApi.login(user);
        String accessToken = response.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;
        String newEmail = generateRandomEmail();
        String newPassword = generateRandomPassword();
        ValidatableResponse data = userApi.update(new UserCredentials(newEmail, newPassword), bearerToken);
        data.assertThat().statusCode(SC_OK);
        data.assertThat().body("success", equalTo(true));
    }
    @Test
    @DisplayName("change authorized user email")
    @Description("testing user modifying only email")
    public void changeUserEmailWithAuth() {
        ValidatableResponse response = userApi.login(user);
        String accessToken = response.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;
        String newEmail = generateRandomEmail();
        ValidatableResponse data = userApi.update(new UserCredentials(newEmail, user.getPassword()), bearerToken);
        data.assertThat().statusCode(SC_OK);
        data.assertThat().body("success", equalTo(true));
    }
    @Test
    @DisplayName("change authorized user password")
    @Description("testing user modifying only password")
    public void changeUserPasswordWithAuth() {
        ValidatableResponse response = userApi.login(user);
        String accessToken = response.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;

        String newPassword = generateRandomPassword();
        ValidatableResponse data = userApi.update(new UserCredentials(user.getEmail(), newPassword), bearerToken);
        data.assertThat().statusCode(SC_OK);
        data.assertThat().body("success", equalTo(true));
    }
    @Test
    @DisplayName("change unauthorized user information")
    @Description("check if unauthorized user can be modified")
    public void changeUserEmailPasswordWithOutAuth() {
        ValidatableResponse response = userApi.login(user);
        bearerToken = " ";
        String newEmail = generateRandomEmail();
        String newPassword = generateRandomPassword();
        ValidatableResponse data = userApi.update(new UserCredentials(newEmail, newPassword), bearerToken);
        data.assertThat().statusCode(SC_UNAUTHORIZED);
        data.assertThat().body("success", equalTo(false ));

    }
}

