package com.lemakhno.mytests;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemakhno.mytests.entity.GetCoursesResponse;

import io.restassured.path.json.JsonPath;

public class SerialDeserialSimpleTest {
    
    @Test
    public void test1() throws IOException {

        // String response = TestUtil.jsonFileToString("src/json_examples/rahulCoursesJson.json");

        ObjectMapper objectMapper = new ObjectMapper();
        
        GetCoursesResponse responsePojo = objectMapper.readValue(new File("src/json_examples/rahulCoursesJson.json"), GetCoursesResponse.class);

        System.out.println(responsePojo.getCourses().getApi());
    }

    @Test
    public void test2() {

        String response = TestUtil.jsonFileToString("src/json_examples/rahulCoursesJson.json");
        JsonPath jsonPath = new JsonPath(response);
        Map<String, Object> body = jsonPath.get("courses");
        System.out.println(body);
    }
}


