
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest extends UserApi {

    private UserApi userApi;
    private User user;
    private ValidatableResponse validatableResponse;
    String bearerToken;

    @BeforeClass
    public static void globalSetup() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }
    @Before
    public void setUp() {
        userApi = new UserApi();
        user = new User("raikiri2@yandex.ru", "konoha123", "Kakashi");
    }

    @After
    public void cleanData() {
        userApi.delete(bearerToken);
    }

    @Test
    @DisplayName("creating user with random data")
    @Description("using random generator testing creating user in the system")
    public void createUserWithValidData() {
        User user = UserGenerator.getRandom();
        userApi.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("accessToken", notNullValue());
    }
    @Test
    @DisplayName("creating user with data that already exist")
    @Description("creating user that already exist leading to code 403")
    public void createUserWithDataAlreadyExist() {
        UserApi userApi = new UserApi();
        userApi.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("creating user without name")
    @Description("checking if user can be created without one field-name")
    public void createUserWithoutName() {
        User user = new User("test-dat@yandex.ru", "password", " ");
        userApi.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("creating user without password")
    @Description("checking if user can be created without password field")
    public void createUserWithoutPassword() {
        User user = new User("test-dat@yandex.ru", "", "KakashiHatake");
        userApi.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("creating user without email")
    @Description("checking if user can be created without email field")
    public void createUserWithoutEmail() {
        User user = new User("", "password123", "KakashiHatake");
        userApi.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN);
    }
}
