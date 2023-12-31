
package api;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

public class APIAutomationSample {

    public static void main(String[] args) {

        /**
         * Response is an interface coming from the RestAssured library
         * The Response variable "response" stores all the components of API calls
         * including the request, and the response
         * API calls in RestAssured is written with BDD flow
         */

        Response response;
        Faker faker = new Faker();

        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();

        // Creating the post request
        response = RestAssured.given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 7f2d18f2290e5f8d942aba191eac25fc54502fcd52375eda4e4c4d51e78c032b")
                .body("{\n" +
                        "    \"name\": \"" + name + "\",\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when().post("https://gorest.co.in/public/v2/users")
                .then().log().all().extract().response();

        int userId = response.jsonPath().getInt("id");

        System.out.println("user id is = " + userId);

        // Creating the get request to fetch user
        response = RestAssured.given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 7f2d18f2290e5f8d942aba191eac25fc54502fcd52375eda4e4c4d51e78c032b")
                .when().get("https://gorest.co.in/public/v2/users/" + userId)
                .then().log().all().extract().response();

        String responseName = response.jsonPath().getString("name");
        String responseEmail = response.jsonPath().getString("email");
        Assert.assertEquals(responseName, name);
        Assert.assertEquals(responseEmail, email);


//        // Get all user
//        response = RestAssured.given().log().all()
//                .header("Content-Type", "application/json")
//                .header("Authorization", "Bearer 7f2d18f2290e5f8d942aba191eac25fc54502fcd52375eda4e4c4d51e78c032b")
//                .when().get("https://gorest.co.in/public/v2/users/")
//                .then().log().all().extract().response();

        response = RestAssured.given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 7f2d18f2290e5f8d942aba191eac25fc54502fcd52375eda4e4c4d51e78c032b")
                .body("{\n" +
                        "    \"name\": \"" + faker.name().fullName() + "\",\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when().patch("https://gorest.co.in/public/v2/users/" + userId)
                .then().log().all().extract().response();

        response = RestAssured.given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 7f2d18f2290e5f8d942aba191eac25fc54502fcd52375eda4e4c4d51e78c032b")
                .when().delete("https://gorest.co.in/public/v2/users/" + userId)
                .then().log().all().extract().response();
    }
}