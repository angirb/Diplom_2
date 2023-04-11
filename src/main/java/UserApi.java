import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.*;


public class UserApi extends Main {

    private static final String USER_URI = BASE_URI + "/auth"; // need to double-check correct API

    @Step
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(USER_URI + "/register")
                .then();
    }

    @Step
    public ValidatableResponse login(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(USER_URI + "/login")
                .then();
    }


    @Step
    public ValidatableResponse update(UserCredentials userCredentials, String accessToken) {

            return given()
                    .header("authorization", accessToken)
                    .spec(getBaseReqSpec())
                    .auth().oauth2(accessToken.replace("Bearer ", ""))
                    .and()
                    .body(userCredentials)
                    .when()
                    .patch(USER_URI + "/user")
                    .then();
    }


    @Step
    public void delete(String bearerToken) {
        if (bearerToken == null || bearerToken == "") {
            return;
        }
        given()
                .spec(getBaseReqSpec())
                .auth().oauth2(bearerToken.replace("Bearer ", ""))
                .when()
                .delete(BASE_URI + "/user")
                .then()
                .statusCode(202);
    }
}
