package jungle.spaceship;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
public class dbSettingTests {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_USERNAME;
    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;

    @Test
    public void testConnection() {

        try(Connection connection =
                    DriverManager.getConnection(
                            DB_URL,
                            DB_USERNAME,
                            DB_PASSWORD)){
            System.out.println(connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
