package api.scripts;

import api.pojo_classes.go_rest.CreateGoRestUser;
import api.pojo_classes.go_rest.UpdateGoRestUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigReader;

import static org.hamcrest.Matchers.*; //assertion library

public class GoRest {

    Response response;
    Faker faker = new Faker();
    ObjectMapper objectMapper = new ObjectMapper();

    String updatedUserName;
    String updatedUserEmail;
    String goRestGender;
    String goRestStatus;

    RequestSpecification baseSpec;

    @BeforeMethod
    public void setAPI(){

        //RestAssured.baseURI = ConfigReader.getProperty("GoRestBaseURI");

        baseSpec = new RequestSpecBuilder().log(LogDetail.ALL)
                .setBaseUri(ConfigReader.getProperty("GoRestURI"))
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", ConfigReader.getProperty("GoRestToken"))
                .build();
    }
    @Test
    public void GoRestCRUD() throws JsonProcessingException {

        CreateGoRestUser createGoRestUser = new CreateGoRestUser();

        createGoRestUser.setName("Tech Global");
        createGoRestUser.setGender("male");
        createGoRestUser.setEmail(faker.internet().emailAddress());
        createGoRestUser.setStatus("active");


        UpdateGoRestUser updateGoRestUser = new UpdateGoRestUser();

        response = RestAssured.given()
                .spec(baseSpec)
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createGoRestUser))
                .when().post( "/public/v2/users")
                .then().log().all()
                .assertThat().statusCode(201).time(Matchers.lessThan(4000L))
                .body("name", equalTo(createGoRestUser.getName()))
                .extract().response();


        String responseName = response.jsonPath().getString("name");
        int goRest_id = response.jsonPath().getInt("id");
        String requestName = createGoRestUser.getName();

        Assert.assertEquals(responseName, requestName);


        response = RestAssured.given()
                .spec(baseSpec)
                .when().get( "/public/v2/users/" + goRest_id)
                .then().log().all().extract().response();

        /**
         * 1. First, crate POJO class called UpdateGoRestUser and create private name and email fields.
         * 2. Create getters and setters from those fields.
         * 3. After you are done, go back to our current test class, and create a UpdateGoRestUser object.
         * 4. And set values for name, and the email.
         * 5. Then serialize those objects to JSON body.
         * 6. Create a PUT API call, and provide the body.
         */


        updateGoRestUser.setName(faker.lordOfTheRings().character());
        updateGoRestUser.setEmail(faker.internet().emailAddress());

        String updatedGoRestUserJSON = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updateGoRestUser);

        response = RestAssured.given()
                .spec(baseSpec)
                .body(updatedGoRestUserJSON)
                .when().put("/public/v2/users/" + goRest_id)
                .then().log().all().extract().response();


        // Fetching the values we set on setters using getters we have in POJO classes
        updatedUserName = updateGoRestUser.getName();
        updatedUserEmail = updateGoRestUser.getEmail();
        goRestGender = createGoRestUser.getGender();
        goRestStatus = createGoRestUser.getStatus();

        // De-serializing the response object from JSON body to assert
        String actualName = response.jsonPath().getString("name");
        String actualEmail = response.jsonPath().getString("email");
        String actualGender = response.jsonPath().getString("gender");
        String actualStatus = response.jsonPath().getString("status");

        String[] requestValues = { updatedUserName, updatedUserEmail, goRestGender, goRestStatus };
        String[] actualValues = { actualName, actualEmail, actualGender, actualStatus };

        // Validating each response object with what we requested.
        for (int i = 0; i < requestValues.length; i++) {
            Assert.assertEquals(actualValues[i], requestValues[i]);
        }

        response = RestAssured.given()
                .spec(baseSpec)
                .when().delete("/public/v2/users/" + goRest_id)
                .then().log().all().extract().response();
    }
}
