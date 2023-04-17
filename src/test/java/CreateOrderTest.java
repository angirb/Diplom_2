import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class CreateOrderTest extends OrderApi {

    private OrderApi orderApi;
    private User user;
    private Order order;
    private UserApi userApi;
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
        orderApi = new OrderApi();
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


    @DisplayName("testing authorized user creating order with ingredients")
    @Description("testing if authorized user can create an order")
    @Test
    public void createOrderTestAuthUser() {

        ValidatableResponse loginResponse = new UserApi().login(user);
        String accessToken = loginResponse.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;
        ValidatableResponse getResponse = new OrderApi().ingredients();
        String[] ingredients = new String[2];
        ingredients[0] = getResponse.extract().path("data._id[0]");
        ingredients[1] = getResponse.extract().path("data._id[1]");

        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderApi.createOrder(accessToken, order);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");

        assertEquals("некорректный код состояния", 200, statusCode);
        assertTrue(bodyJSON);
    }


    @DisplayName("testing authorized user creating order without ingredients")
    @Description("checking if authorized user can create order without ingredients")
    @Test
    public void createOrderWithOutIngredients() {

        ValidatableResponse loginResponse = new UserApi().login(user);
        String accessToken = loginResponse.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;
        String[] ingredients = {null};

        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderApi.createOrder(accessToken, order);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");
        assertEquals("некорректный код состояния", 400, statusCode);
        assertFalse(bodyJSON);

    }

    @DisplayName("test with unauthorized user creating order with ingredients")
    @Description("creating order by unauthorized user with ingredients")
    @Test
    public void createOrderTestWithOutAuthWithIngredients() {

        ValidatableResponse loginResponse = new UserApi().login(user);
        String accessToken = loginResponse.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;
        ValidatableResponse getResponse = new OrderApi().ingredients();
        String[] ingredients = new String[2];
        ingredients[0] = getResponse.extract().path("data._id[0]");
        ingredients[1] = getResponse.extract().path("data._id[1]");

        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderApi.createOrderWithOutAuth(order);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");
        assertEquals("некорректный код состояния", 200, statusCode);
        assertTrue(bodyJSON);
    }

    @DisplayName("test with unauthorized user creating order without ingredients")
    @Description("creating order by unauthorized user without ingredients")
    @Test
    public void createOrderTestWithOutAuthWithOutIngredients() {

        ValidatableResponse loginResponse = new UserApi().login(user);
        String accessToken = loginResponse.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;
        String[] ingredients = {null};

        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderApi.createOrderWithOutAuth(order);
        int statusCode = orderResponse.extract().statusCode();
        boolean bodyJSON = orderResponse.extract().path("success");
        assertEquals("некорректный код состояния", 400, statusCode);
        assertFalse(bodyJSON);

    }

    @DisplayName("testing authorized user creating order with an incorrect hash of ingredients")
    @Description("check if authorized user can create an order when hash of ingredients is incorrect")
    @Test
    public void createOrderTestAuthUserIngredientsNotValid() {

        ValidatableResponse loginResponse = new UserApi().login(user);
        String accessToken = loginResponse.extract().path("accessToken");
        bearerToken = "Bearer " + accessToken;

        String[] ingredients = new String[2];
        ingredients[0] = RandomStringUtils.randomAlphabetic(15);
        ingredients[1] = RandomStringUtils.randomAlphabetic(15);

        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderApi.createOrder(accessToken, order);
        int statusCode = orderResponse.extract().statusCode();
        assertEquals("некорректный код состояния", 500, statusCode);

    }
}


