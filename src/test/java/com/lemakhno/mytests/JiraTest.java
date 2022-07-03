package com.lemakhno.mytests;

// import static io.restassured.matcher.RestAssuredMatchers.*;
// import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class JiraTest {

    private static final String BASE_URL = "http://localhost:8100";
    private static final String USERNAME = "voroshilov-klim";
    private static final String PASSWORD = "Bastogne1337";

    private static SessionFilter sessionFilter = new SessionFilter();
    
    private static String authCookie;

    private static String issueId;
    // private static String commentId;

    @Test
    public static void testLogin() {

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("username", USERNAME);
        credentials.put("password", PASSWORD);

        Response response =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .filter(sessionFilter)
        .when()
            .post(BASE_URL + "/rest/auth/1/session")
        .then()
            .assertThat()
            .statusCode(200)
        .extract()
            .response();

        String sessionName = response.jsonPath().getString("session.name");
        String value = response.jsonPath().getString("session.value");

        String cookie = sessionName + "=" + value;

        // System.out.println(">>> COOKIE: " + cookie);

        authCookie = cookie;

        System.out.println(">>> LOGIN OK");
    }

    @Test
    public static void testCreateIssue() {

        String requestBody = TestUtil.jsonFileToString("src/json_examples/newIssueBody.json");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .filter(sessionFilter)
            //.header("Cookie", authCookie)
            .body(requestBody)
        .when()
            .post(BASE_URL + "/rest/api/2/issue")
        .then()
            .assertThat()
            .statusCode(201)
        .extract()
        .response();

        String newIssueId = response.jsonPath().getString("id");
        String key = response.jsonPath().getString("key");

        System.out.println(">>> NEW ISSUE ID: " + newIssueId);

        Assert.assertNotNull(newIssueId);
        Assert.assertTrue(key.startsWith("TP"));

        issueId = newIssueId;

        System.out.println(">>> CREATE ISSUE OK");
    }

    @Test
    public static void testAddCommentToIssue() {

        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("body", "Comment from Rest-Assured");

        Response response =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .pathParam("issueId", issueId)
            .body(reqBody)
            .filter(sessionFilter)
        .when()
            .post(BASE_URL + "/rest/api/2/issue/{issueId}/comment")
        .then()
            .assertThat()
            .statusCode(201)
        .extract()
            .response();

        String newCommentId = response.jsonPath().getString("id");

        Assert.assertNotNull(newCommentId);

        // commentId = newCommentId;

        System.out.println(">>> ADD COMMENT TO ISSUE OK");
    }

    @Test
    public static void testAddAttachmentToIssue() {

        given()
            .contentType(ContentType.MULTIPART)
            .accept(ContentType.JSON)
            .multiPart(new File("src/json_examples/porox.jpg"))
            .header("X-Atlassian-Token", "no-check")
            .pathParam("issueId", issueId)
            .filter(sessionFilter)
        .when()
            .post(BASE_URL + "/rest/api/2/issue/{issueId}/attachments")
        .then()
            .assertThat()
            .statusCode(200);

        System.out.println(">>> ADD ATTACHMENT TO ISSUE OK");
    }

    @Test
    public static void testGetIssueDetails() {

        String id = "10015";

        Response response =
        given()
            .accept(ContentType.JSON)
            .pathParam("issueId", id)
            .queryParam("fields", "comment")
            .filter(sessionFilter)
        .when()
            .get(BASE_URL + "/rest/api/2/issue/{issueId}")
        .then()
            .assertThat()
            .statusCode(200)
        .extract()
            .response();

        int commentsAmount = response.jsonPath().getInt("fields.comment.comments.size()");
        
        for (int i = 0; i < commentsAmount; i++) {
            String commentText = response.jsonPath().getString("fields.comment.comments[" + i + "].body");
            String commentId = response.jsonPath().getString("fields.comment.comments[" + i + "].id");
            System.out.println(">>> Comment " + commentId + " '" + commentText + "'");
        }

        System.out.println(">>> ADD ATTACHMENT TO ISSUE OK");
    }

    @Test
    public static void testDeleteIssue() {

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("Cookie", authCookie)
            .pathParam("issueId", issueId)
            .filter(sessionFilter)
        .when()
            .delete(BASE_URL + "/rest/api/2/issue/{issueId}")
        .then()
            .assertThat()
            .statusCode(204);

        System.out.println(">>> DELETE ISSUE OK");
    }

    public static void main(String[] args) {
        
        testLogin();
        // testCreateIssue();
        // testAddCommentToIssue();
        // testAddAttachmentToIssue();
        testGetIssueDetails();
        // . . .
        // testDeleteIssue();
    }
}


