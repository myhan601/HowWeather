package howWeather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) {
        try {
        	CourseWeather[][] arr = WeatherApi.parseJSONToCourseWeather(310);
        	
        	for (int j = 0; j < arr[0].length; j++) {
        		for (int i = 0; i < arr.length; i++) {
        			System.out.println(i + "-" + j + " " + arr[i][j].getTm() + " " + arr[i][j].getSpotName() + "\t");
        		}
        		System.out.println();
        	}
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
