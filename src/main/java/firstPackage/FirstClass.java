package firstPackage;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FirstClass {

    private static String punkBaseUri = "https://api.punkapi.com/v2";

    private static Logger logger = Logger.getLogger(FirstClass.class.getName());

    private static String jsonFileToString(String path) {

        String result = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
    
    @Test
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

    @Test
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

    @Test
    public static void test3() {

        RestAssured.baseURI = punkBaseUri;

        String responseString =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/beers?page=2&per_page=5")
        .then()
            .assertThat()
            .statusCode(200)
        .extract()
            .response()
            .asString();

        JsonPath jsonPath = new JsonPath(responseString);
        List<Map<String, Object>> namesList = jsonPath.getList("$");

        System.out.println(">>> " + namesList);

        logger.info(">>> Test 3 passed");
    }

    @Test
    public static void test4() {

        String responseBody = jsonFileToString("src/json_examples/complexJson.json");

        JsonPath jsonPath = new JsonPath(responseBody);

        Integer size = jsonPath.getInt("courses.size()");
        Integer purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");

        Integer actualPurchaseAmount = 0;

        for (int i = 0; i < size; i++) {
            Integer price = jsonPath.getInt("courses[" + i + "].price");
            Integer copies = jsonPath.getInt("courses[" + i + "].copies");

            actualPurchaseAmount += price * copies;
        }

        Assert.assertEquals(actualPurchaseAmount, purchaseAmount, "Sum of purchases from JSON must match the total sales amount");

        logger.info(">>> Test 4 passed");
    }

    @Test
    public static void test5() {

        String response = jsonFileToString("src/json_examples/nestedArrays.json");

        JsonPath jsonPath = new JsonPath(response);

        List<List<String>> list = jsonPath.getList("complexArray");

        System.out.println(list);
    }

    public static void main(String[] args) {
        
        // test1();
        // test2();
        // test3();
        test4();
        // test5();

    }
}


