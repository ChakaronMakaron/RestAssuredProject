package firstPackage;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.testng.Assert;

public class FirstClass {

    private static String punkBaseUri = "https://api.punkapi.com/v2";

    private static Logger logger = Logger.getLogger(FirstClass.class.getName());
    
    public static void test1() {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        given()
            .queryParam("key", "qacklick123")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/")
        .then()
            .assertThat()
            .statusCode(200);

        logger.info(">>> Test 1 passed");
    }

    public static void test2() {

        RestAssured.baseURI = punkBaseUri;

        String responseString =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/beers/1")
        .then()
            .assertThat()
            .statusCode(200)
            .header("Server", "cloudflare")
        .extract()
            .response()
            .asString();

        JsonPath jsonPath = new JsonPath(responseString);

        List<HashMap<String, Object>> list = jsonPath.getList("$");

        HashMap<String, Object> beer = list.get(0);

        Assert.assertEquals(beer.get("name"), "Buzz", "Response should be \"Buzz\"");

        logger.info(">>> Test 2 passed");
    }

    public static void main(String[] args) {
        
        // test1();
        test2();

    }
}


