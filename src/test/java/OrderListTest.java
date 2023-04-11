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

import static org.junit.Assert.assertEquals;

public class OrderListTest {
    private OrderApi orderApi;
    private User user;
    private Order order;
    private UserApi userApi;
    private String bearerToken;
    private String accessToken;
    OrderApi ingredients;
    private ValidatableResponse validatableResponse;

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
        ValidatableResponse getResponse = orderApi.ingredients();
        String[] ingredients = new String[2];
        ingredients[0] = getResponse.extract().path("data._id[0]");
        ingredients[1] = getResponse.extract().path("data._id[1]");

        user = UserGenerator.getRandom();
        accessToken = userApi.create(user).extract().path("accessToken");
        order = new Order(ingredients);
        orderApi.createOrder(accessToken, order);
    }
    @After
    public void cleanData() {
        if (validatableResponse != null) {
            String bearerToken = validatableResponse.extract().path("accessToken");
            userApi.delete(bearerToken);
        }
    }
    @Test
    @DisplayName("get order list with authorization")
    @Description("check if order list can be pulled by authorized user")
    public void authUserCanGetOrderList() {
        ValidatableResponse response = orderApi.getOrderDataList(accessToken);
        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");
        assertEquals("некорректный код состояния", 200, statusCode);
        assertEquals("некорректное тело JSON", true, bodyJSON);
    }
    @Test
    @DisplayName("get order list without authorization")
    @Description("check if order list can be pulled without registration")
    public void unAuthUserCanGetOrderList() {
        ValidatableResponse response = orderApi.getOrderDataListWithOutAuth();
        int statusCode = response.extract().statusCode();
        boolean bodyJSON = response.extract().path("success");
        assertEquals("некорректный код состояния", 401, statusCode);
        assertEquals("некорректное тело JSON", false, bodyJSON);
    }

}
