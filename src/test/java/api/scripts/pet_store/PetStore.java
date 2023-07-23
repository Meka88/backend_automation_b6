package api.scripts.pet_store;

import api.pojo_classes.pet_store.AddPet;
import api.pojo_classes.pet_store.Category;
import api.pojo_classes.pet_store.Tags;
import api.pojo_classes.pet_store.UpdatePet;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.Arrays;
import static org.hamcrest.Matchers.*;

public class PetStore {
    Response response;
    private RequestSpecification baseSpec;

    @BeforeMethod
    public void setAPI() {

        baseSpec = new RequestSpecBuilder().log(LogDetail.ALL)
                .setBaseUri(ConfigReader.getProperty("PetStoreBaseURI"))
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void petStoreAPI(){
        Category category = Category.builder()
                .id(10).name("horse").build();

        Tags tags = Tags.builder()
                .id(11).name("unicorn")
                .build();

        AddPet addPet = AddPet.builder()
                .id(10)
                .category(category).name("mustang")
                .photoUrls(Arrays.asList("Horse photo URL"))
                .tags(Arrays.asList(tags)).status("available").build();

        response = RestAssured.given()
                .spec(baseSpec)
                .body(addPet)
                .when().post("/v2/pet").then().log().all()
                .assertThat().statusCode(200)
                .time(Matchers.lessThan(4000l))
                .body("tags[0].name", equalTo(tags.getName()))
                .extract().response();

        int user_id = response.jsonPath().getInt("id");

        category = Category.builder()
                .id(10).name("dog").build();

        tags = Tags.builder().id(11).name("frog")
                .build();

        UpdatePet updatePet = UpdatePet.builder()
                .id(10)
                .category(category).name("Snowflake")
                .photoUrls(Arrays.asList("Bunny Photo URL"))
                .tags(Arrays.asList(tags)).status("sold")
                .build();

        response = RestAssured.given()
                .spec(baseSpec)
                .body(updatePet)
                .when().put("/v2/pet/").then().log().all()
                .assertThat().statusCode(200)
                .time(Matchers.lessThan(4000l))
                .body("category.name", equalTo(category.getName()))
                .extract().response();
    }
}
