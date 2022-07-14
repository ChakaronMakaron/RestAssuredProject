package com.lemakhno.mytests;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemakhno.mytests.entity.Course;
import com.lemakhno.mytests.entity.GetCoursesResponse;
import com.lemakhno.mytests.entity.serializationbigbody.BigBodyRequest;
import com.lemakhno.mytests.entity.serializationbigbody.Location;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

@Test(groups = {"dummytests"})
public class SerialDeserialSimpleTest {
    
    @Test(groups = {"IOtests"})
    public void test1() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        
        GetCoursesResponse getCoursesResponse = objectMapper.readValue(new File("src/json_examples/rahulCoursesJson.json"), GetCoursesResponse.class);

        String courseToFind = "SoapUI Webservices testing";

        List<List<Course>> allCoursesList = new ArrayList<>();
        allCoursesList.add(getCoursesResponse.getCourses().getApi());
        allCoursesList.add(getCoursesResponse.getCourses().getMobile());
        allCoursesList.add(getCoursesResponse.getCourses().getWebAutomation());

        allCoursesList.stream().flatMap(coursesList -> coursesList.stream()).forEach(course -> {
            if (course.getCourseTitle().equals(courseToFind)) {
                System.out.println(">>> REQUESTED COURSE PRICE IS " + course.getPrice() + "$");
            }
        });
    }

    @Test(groups = {"IOtests"})
    public void test2() {

        String response = TestUtil.jsonFileToString("src/json_examples/rahulCoursesJson.json");
        JsonPath jsonPath = new JsonPath(response);
        Map<String, Object> body = jsonPath.get("courses");
        String penis = jsonPath.getString("penis");
        System.out.println(">>> PENIS: " + penis);
        System.out.println(body);
    }

    @Test(groups = {"IOtests"})
    public void test3() throws JsonProcessingException {

        Integer[] intArr = {1, 2, 3, 4, 5};

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("first", "value1");
        bodyMap.put("second", "value2");
        bodyMap.put("intArr", intArr);

        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(objectMapper.writeValueAsString(bodyMap));
    }

    @Test(groups = {"normtests"})
    public void test4() {

        RestAssured.baseURI = "https://www.google.com";

        given().log().all()
            .queryParam("param", List.of("val1", "val2", "val3"))
        .when()
            .get();
    }

    @Test(groups = {"normtests"})
    public void test5() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        RestAssured.baseURI = "https://www.google.com";

        Location location = new Location();
        location.setLat(-38.383494F);
        location.setLng(33.427362F);

        BigBodyRequest requestBody = new BigBodyRequest();
        requestBody.setLocation(location);
        requestBody.setAccuracy(50);
        requestBody.setName("Frontline house");
        requestBody.setPhone_number("(+91) 983 893 39 37");
        requestBody.setAddress("29, side layout, cohen 09");
        requestBody.setTypes(List.of("shoe park", "shop"));
        requestBody.setWebsite("http://google.com");
        requestBody.setLanguage("French-IN");

        String request = objectMapper.writeValueAsString(requestBody);

        JsonPath jsonPath = new JsonPath(request);

        System.out.println(jsonPath.prettify());

        // Response response =
        given().log().all()
            .body(requestBody)
        .when()
            .get()
        .then()
            .extract()
            .response();
    }

    @Test
    public void test6() {

        
    }
}


