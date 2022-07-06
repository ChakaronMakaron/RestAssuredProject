package com.lemakhno.mytests;

// import static io.restassured.matcher.RestAssuredMatchers.*;
// import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.lemakhno.mytests.entity.libraryentity.BookCreationRequest;
import com.lemakhno.mytests.entity.libraryentity.BookCreationResponse;
import com.lemakhno.mytests.entity.libraryentity.BookDeletingResponse;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class LibraryTestSerialDeserial {

    private static final String BASE_URL = "http://216.10.245.166";

    @DataProvider(name = "BulkBooksCreation")
    public String[][] getData() {
        return new String[][] {{"11", "11"}, {"22", "22"}, {"33", "33"}};
    }

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
    
    @Test
    public void testCreateNewBook() {

        BookCreationRequest requestBody = new BookCreationRequest();
        requestBody.setName("NewBook");
        requestBody.setIsbn("333");
        requestBody.setAisle("12");
        requestBody.setAuthor("LeMakhno");

        RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

        RequestSpecification request = given()
            .log().all()
            .spec(requestSpec)
            .body(requestBody);

        BookCreationResponse response = request.when().post("/Library/Addbook.php")
            .then()
            .spec(responseSpec)
            .extract().response().as(BookCreationResponse.class);

        String message = response.getMsg();
        String newBookId = response.getId();

        Assert.assertEquals(message, "successfully added");
        Assert.assertNotNull(newBookId, "created book id MUST NOT be NULL");

        System.out.println(">>> TEST CREATE BOOK PASSED");

        Map<String, Object> deleteRequestBody = new HashMap<>();
        deleteRequestBody.put("ID", newBookId);

        BookDeletingResponse deleteResponse =
        given().log().all()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(deleteRequestBody, ObjectMapperType.JACKSON_2)
        .when()
            .post("/Library/DeleteBook.php")
        .then().log().all()
            .spec(responseSpec)
        .extract()
            .response()
            .as(BookDeletingResponse.class);

        String deleteMessage = deleteResponse.getMsg();

        Assert.assertEquals(deleteMessage, "book is successfully deleted");

        System.out.println(">>> SUCCESSFULLY DELETED BOOK");
    }

    @Test
    public void testSpecBuilder() {

        BookCreationRequest requestBody = new BookCreationRequest();
        requestBody.setName("NewBook");
        requestBody.setIsbn("333");
        requestBody.setAisle("12");
        requestBody.setAuthor("LeMakhno");

        BookCreationResponse response =
        given().log().all()
            .contentType(ContentType.JSON)
            .accept(ContentType.ANY)
            .body(requestBody)
        .when()
            .post("/Library/Addbook.php")
        .then().log().all()
            .assertThat()
            .statusCode(200)
        .extract()
            .response()
            .as(BookCreationResponse.class);

        String message = response.getMsg();
        String newBookId = response.getId();

        Assert.assertEquals(message, "successfully added");
        Assert.assertNotNull(newBookId, "created book id MUST NOT be NULL");

        System.out.println(">>> TEST CREATE BOOK PASSED");

        Map<String, Object> deleteRequestBody = new HashMap<>();
        deleteRequestBody.put("ID", newBookId);

        BookDeletingResponse deleteResponse =
        given().log().all()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(deleteRequestBody, ObjectMapperType.JACKSON_2)
        .when()
            .post("/Library/DeleteBook.php")
        .then().log().all()
            .assertThat()
            .statusCode(200)
        .extract()
            .response()
            .as(BookDeletingResponse.class);

        String deleteMessage = deleteResponse.getMsg();

        Assert.assertEquals(deleteMessage, "book is successfully deleted");

        System.out.println(">>> SUCCESSFULLY DELETED BOOK");
    }

    @Test
    @Ignore
    public void testGetBooksByAuthorName() {

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


