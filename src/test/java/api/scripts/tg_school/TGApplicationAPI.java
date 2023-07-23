package api.scripts.tg_school;

import api.pojo_classes.tg_school.CreateStudent;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigReader;
import utils.DBUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TGApplicationAPI {

    Response response;
    private RequestSpecification baseSpec;

    Faker faker = new Faker();

    @BeforeMethod
    public void setTest(){
        baseSpec = new RequestSpecBuilder().log(LogDetail.ALL)
                .setBaseUri(ConfigReader.getProperty("TGSchoolBaseURI"))
                .setContentType(ContentType.JSON)
                .build();

        DBUtil.createDBConnection();
    }

    @Test
    public void TGAPIProject(){
        CreateStudent createStudent = CreateStudent.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .dob("1999-05-01")
                .build();

        // create student
        response  = RestAssured.given()
                .spec(baseSpec)
                .body(createStudent)
                .when().post("/students")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();


        int id = response.jsonPath().getInt("id");
        // validate is student created in DB
        String query = "SELECT * FROM student WHERE id = " + id;

        // we get all rows
        List<List<Object>> queryResultList = DBUtil.getQueryResultList(query);

        // our query will return a single row, we fetch the first element from list of list
        // which represents our query result
        List<Object> dbResult = queryResultList.get(0);



        // to fix our error above we are casting our id that is coming from DB as BigDecimal to int
        BigDecimal dbId = (BigDecimal) dbResult.get(0);
        int dbIdInt = dbId.intValue();

        List<Object> formattedDBResult = new ArrayList<>(dbResult);
        formattedDBResult.set(0, dbIdInt);

        for(Object o : formattedDBResult){
            System.out.println(o);
        }
        Assert.assertEquals(formattedDBResult, Arrays.asList(id, createStudent.getDob(), createStudent.getEmail(),
                createStudent.getFirstName(), createStudent.getLastName()));

        // create delete request
        response  = RestAssured.given()
                .spec(baseSpec)
                .when().delete("/students/" + id)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();

        queryResultList = DBUtil.getQueryResultList(query);

        Assert.assertTrue(queryResultList.isEmpty(), " The student with id: " + id + " is not deleted from the database.");
    }
}
