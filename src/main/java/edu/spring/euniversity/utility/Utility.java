package edu.spring.euniversity.utility;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class Utility {
    public <T> String buildJson(T elem) {
        Gson gson = new Gson();
        return gson.toJson(elem);
    }
}
