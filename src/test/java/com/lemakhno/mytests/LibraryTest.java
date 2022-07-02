package com.lemakhno.mytests;

// import static io.restassured.matcher.RestAssuredMatchers.*;
// import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class LibraryTest {

    private static final String BASE_URL = "http://216.10.245.166";

    private static String newBookId;

    @DataProvider(name = "BulkBooksCreation")
    public String[][] getData() {
        return new String[][] {{"11", "11"}, {"22", "22"}, {"33", "33"}};
    }
    
    @Test
    public static void testCreateNewBook() {

        String requestBody = TestUtil.jsonFileToString("src/json_examples/newBookBody.json");

        String response =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(requestBody)
        .when()
            .post(BASE_URL + "/Library/Addbook.php")
        .then().log().all()
            .assertThat()
            .statusCode(200)
        .extract()
            .response()
            .asString();

        JsonPath jsonPath = new JsonPath(response);
        String message = jsonPath.getString("Msg");
        String id = jsonPath.getString("ID");

        Assert.assertEquals(message, "successfully added");
        Assert.assertNotNull(id, "created book id MUST NOT be NULL");

        newBookId = id;

        System.out.println(">>> TEST CREATE BOOK PASSED");

        Map<String, Object> deleteRequestBody = new HashMap<>();
        deleteRequestBody.put("ID", newBookId);

        String deleteResponse =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(deleteRequestBody)
        .when()
            .post(BASE_URL + "/Library/DeleteBook.php")
        .then()
            .assertThat()
            .statusCode(200)
        .extract()
            .response()
            .asString();

        JsonPath deleteRequestJsonPath = new JsonPath(deleteResponse);
        String deleteMessage = deleteRequestJsonPath.getString("msg");

        Assert.assertEquals(deleteMessage, "book is successfully deleted");

        System.out.println(">>> SUCCESSFULLY DELETED BOOK");
    }

    @Test
    public static void testGetBooksByAuthorName() {

        String response =
        given()
            .queryParam("AuthorName", "Me")
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/Library/GetBook.php")
        .then()
            .statusCode(200)
        .extract()
        .response()
        .asString();

        JsonPath jsonPath = new JsonPath(response);

        List<Map<String, Object>> list = jsonPath.getList("$");

        System.out.println(">>> " + list.get(0));

        System.out.println(">>> TEST PASSED");
    }

    @Test(dataProvider = "BulkBooksCreation")
    public void testBulkBooksCreation(String isbn, String aisle) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "bulkBook");
        requestBody.put("isbn", isbn);
        requestBody.put("aisle", aisle);
        requestBody.put("author", "bulkAuthor");

        String responseBody =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(requestBody)
        .when()
            .post(BASE_URL + "/Library/Addbook.php")
        .then().log().all()
            .assertThat()
            .statusCode(200)
        .extract()
            .response()
            .asString();

        JsonPath responseJsonPath = new JsonPath(responseBody);

        String message = responseJsonPath.getString("Msg");
        String newBookId = responseJsonPath.getString("ID");

        Assert.assertEquals(message, "successfully added");
        Assert.assertNotNull(newBookId, "created book id MUST NOT be NULL");

        // ########## DELETING THE BOOK

        Map<String, Object> deleteRequestBody = new HashMap<>();
        deleteRequestBody.put("ID", newBookId);

        String deleteResponse =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(deleteRequestBody)
        .when()
            .post(BASE_URL + "/Library/DeleteBook.php")
        .then()
            .assertThat()
            .statusCode(200)
        .extract()
            .response()
            .asString();

        JsonPath deleteRequestJsonPath = new JsonPath(deleteResponse);
        String deleteMessage = deleteRequestJsonPath.getString("msg");

        Assert.assertEquals(deleteMessage, "book is successfully deleted", "The book must be deleted after creating");
    }

    public static void main(String[] args) {
        
        // testCreateNewBook();


        // testGetBooksByAuthorName();
    }
}


