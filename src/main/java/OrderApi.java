import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderApi extends Main {

    private final String ORDER_URI = BASE_URI + "/orders";

    @Step
    public ValidatableResponse createOrder(String accessToken, Order order) {
        return given()
                .spec(getBaseReqSpec())
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .body(order)
                .when()
                .post(ORDER_URI)
                .then();
    }
    @Step
    public ValidatableResponse createOrderWithOutAuth(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(ORDER_URI)
                .then();
    }
//    @Step
//    public ValidatableResponse orderData() {
//        return given()
//                .spec(getBaseReqSpec())
//                .when()
//                .get(ORDER_URI + "/all")
//                .then();
//    }
    @Step
    public ValidatableResponse getOrderDataList(String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .when()
                .get(ORDER_URI)
                .then();
    }
    @Step
    public ValidatableResponse getOrderDataListWithOutAuth() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI)
                .then();
    }
    @Step
    public ValidatableResponse ingredients() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(BASE_URI + "/ingredients")
                .then();
    }
}
