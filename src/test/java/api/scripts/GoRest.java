package api.scripts;

import api.pojo_classes.go_rest.CreateGoRestUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigReader;

public class GoRest {

    Response response;
    Faker faker = new Faker();
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeMethod
    public void setAPI(){
        RestAssured.baseURI = ConfigReader.getProperty("GoRestBaseURI");
    }
    @Test
    public void GoRestCRUD() throws JsonProcessingException {

        CreateGoRestUser createGoRestUser = new CreateGoRestUser();

        createGoRestUser.setName("Tech Global");
        createGoRestUser.setGender("male");
        createGoRestUser.setEmail(faker.internet().emailAddress());
        createGoRestUser.setStatus("active");


        response = RestAssured.given().log().all()
//                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createGoRestUser))
                .when().post( "/public/v2/users")
                .then().log().all().extract().response();


        String actualName = response.jsonPath().getString("name");
        int goRest_id = response.jsonPath().getInt("id");
        String requestName = createGoRestUser.getName();

        Assert.assertEquals(actualName, requestName);


        response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .when().get( "/public/v2/users/" + goRest_id)
                .then().log().all().extract().response();

    }
}
