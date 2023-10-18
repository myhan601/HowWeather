package howWeather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {
        try {
        	Timer t1 = new Timer();
        	t1.start();
        	String str = wather();
        	t1.end();
        	System.out.println(t1.toString());
        	
        	t1.start();
        	System.out.println(parseJSONToCourseWeather(str).toString());
        	t1.end();
        	System.out.println(t1.toString());
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    public static String wather() throws IOException {
        String apiUrl = "http://apis.data.go.kr/1360000/TourStnInfoService1/getTourStnVilageFcst1";

        String serviceKey = "y%2Bcyi5tgE7yNDv8T8Mzs3A4iSs6cxMDvHoHkyj9Eoj%2FHOY8d7XxsLzSqU5SDHiFsL771Qvxow9bw%2BeXKPSJ0yA%3D%3D";
        String pageNo = "2";	//페이지 번호
        String numOfRows = "10";	//한 페이지 결과 수
        String dataType = "JSON";	//데이터 타입
        String CURRENT_DATE = "20230916";	//조회하고싶은 날짜
        String HOUR = "24";	//조회하고 싶은 날짜의 시간 날짜
        String COURSE_ID = "10";	//관광 코스ID


        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("CURRENT_DATE","UTF-8") + "=" + URLEncoder.encode(CURRENT_DATE, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("HOUR","UTF-8") + "=" + URLEncoder.encode(HOUR, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("COURSE_ID","UTF-8") + "=" + URLEncoder.encode(COURSE_ID, "UTF-8"));

        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(200 <= conn.getResponseCode() && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result= sb.toString();
        System.out.println(result);

    return result;
    }
    
    public static void dataParsing(String json) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

         // "item" 배열을 순회하며 필요한 정보를 추출
            JsonNode items = rootNode.path("response").path("body").path("items").path("item");
            for (JsonNode item : items) {
                String tm = item.path("tm").asText();
                String thema = item.path("thema").asText();
                String courseId = item.path("courseId").asText();
                String courseAreaId = item.path("courseAreaId").asText();
                String courseAreaName = item.path("courseAreaName").asText();
                String courseName = item.path("courseName").asText();
                String spotAreaId = item.path("spotAreaId").asText();
                String spotAreaName = item.path("spotAreaName").asText();
                String spotName = item.path("spotName").asText();
                int th3 = item.path("th3").asInt();
                int wd = item.path("wd").asInt();
                int ws = item.path("ws").asInt();
                int sky = item.path("sky").asInt();
                int rhm = item.path("rhm").asInt();
                int pop = item.path("pop").asInt();

                // 필요한 정보를 출력 또는 처리
                System.out.println("tm: " + tm);
                System.out.println("thema: " + thema);
                System.out.println("courseId: " + courseId);
                System.out.println("courseAreaId: " + courseAreaId);
                System.out.println("courseAreaName: " + courseAreaName);
                System.out.println("courseName: " + courseName);
                System.out.println("spotAreaId: " + spotAreaId);
                System.out.println("spotAreaName: " + spotAreaName);
                System.out.println("spotName: " + spotName);
                System.out.println("th3: " + th3);
                System.out.println("wd: " + wd);
                System.out.println("ws: " + ws);
                System.out.println("sky: " + sky);
                System.out.println("rhm: " + rhm);
                System.out.println("pop: " + pop);

                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static CourseWeather parseJSONToCourseWeather(String jsonData) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray itemArray = (JSONArray) items.get("item");

            // 여러 개의 아이템 중에서 하나를 선택
            JSONObject firstItem = (JSONObject) itemArray.get(0);

            // JSON 데이터에서 필드 추출
            String tm = (String) firstItem.get("tm");
            String thema = (String) firstItem.get("thema");
            String courseId = (String) firstItem.get("courseId");
            String courseAreaId = (String) firstItem.get("courseAreaId");
            String courseAreaName = (String) firstItem.get("courseAreaName");
            String courseName = (String) firstItem.get("courseName");
            Long spotAreaId = (Long) firstItem.get("spotAreaId");
            String spotAreaName = (String) firstItem.get("spotAreaName");
            String spotName = (String) firstItem.get("spotName");
            Long th3 = (Long) firstItem.get("th3");
            Long wd = (Long) firstItem.get("wd");
            Long ws = (Long) firstItem.get("ws");
            Long sky = (Long) firstItem.get("sky");
            Long rhm = (Long) firstItem.get("rhm");
            Long pop = (Long) firstItem.get("pop");

            // CourseWeather 객체 생성
            CourseWeather courseWeather = new CourseWeather(tm, thema, courseId, courseAreaId, courseAreaName,
                courseName, spotAreaId, spotAreaName, spotName, th3, wd, ws, sky, rhm, pop);

            return courseWeather;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
