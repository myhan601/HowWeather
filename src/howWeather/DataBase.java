package howWeather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
	private static Connection conn;
	
	private static String csvFilePath = "기상청_관광코스별 관광지 상세날씨 조회 지점 정보_20200110.csv"; // CSV 파일 경로
    private static String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String username = "lck0722";
	private static String password = "1234";
	private static String tableName = "course_data";
	
	static {
		try {
            // 데이터베이스 연결
            conn = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private DataBase() {     
	}
	
	private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM user_tables WHERE table_name = '" + tableName + "'");
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count > 0;
    }

	public static void createTable() {
		try {
            if (!tableExists(conn, tableName)) {
            	// 테이블 생성
                Statement stmt = conn.createStatement();
                stmt.execute(getCreateSQL());
                System.out.println("테이블이 생성되었습니다.");
                
                // CSV 파일을 읽어서 데이터베이스에 삽입
                insertCsvData(conn, csvFilePath);
                System.out.println("데이터가 삽입되었습니다.");
            } else {
                System.out.println("테이블이 이미 존재합니다.");
            }
            
            // 데이터베이스 연결 닫기
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
    // 데이터베이스에 테이블 생성
    private static String getCreateSQL() throws SQLException {
        String createTableSQL = "CREATE TABLE " + tableName + "(\r\n"
        		+ "        	    theme_category VARCHAR2(255),\r\n"
        		+ "        	    course_id NUMBER(10),\r\n"
        		+ "        	    tourism_id NUMBER(10),\r\n"
        		+ "        	    region_id NUMBER(10),\r\n"
        		+ "        	    tourism_name VARCHAR2(255),\r\n"
        		+ "        	    longitude VARCHAR2(255),\r\n"
        		+ "        	    latitude VARCHAR2(255),\r\n"
        		+ "        	    course_order NUMBER(10),\r\n"
        		+ "        	    travel_time NUMBER(10),\r\n"
        		+ "        	    indoor_type VARCHAR2(255),\r\n"
        		+ "        	    theme_name VARCHAR2(255)\r\n"
        		+ "        	)";

        return createTableSQL;
    }

    // CSV 파일을 읽어서 데이터베이스에 삽입
    private static void insertCsvData(Connection conn, String csvFilePath) throws SQLException {
        String insertSQL = "INSERT INTO " + tableName + " (theme_category, course_id, tourism_id, region_id, tourism_name, longitude, latitude, course_order, travel_time, indoor_type, theme_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 11) {
                    for (int i = 0; i < 11; i++) {
                        if (i == 0 || i == 4 || i == 5 || i == 6 || i == 9 || i == 10) {
                        	pstmt.setString(i + 1, data[i]);
                        }
                        else {
                        	pstmt.setLong(i + 1, Long.parseLong(data[i]));
                        }
                    }
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
