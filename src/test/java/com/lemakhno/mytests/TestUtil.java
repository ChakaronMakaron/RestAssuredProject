package com.lemakhno.mytests;

import java.io.BufferedReader;
import java.io.FileReader;

public class TestUtil {
    
    public static String jsonFileToString(String path) {

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
}
