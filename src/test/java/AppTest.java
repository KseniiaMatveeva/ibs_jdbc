import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;

import java.sql.*;

public class AppTest {

    private Statement statement;

    @BeforeEach
    @DisplayName("Подключение к БД")
    public void testConnect() throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        this.statement = connection.createStatement();
    }

    @Test
    @DisplayName("Добавление товара в таблицу")
    public void testAdd() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT MAX(FOOD_ID) FROM FOOD");
        rs.next();
        int maxId = rs.getInt(1);
        statement.executeUpdate(
                "INSERT INTO FOOD (FOOD_ID, FOOD_NAME, FOOD_TYPE, FOOD_EXOTIC) VALUES (" +
                (maxId + 1) + ", 'ONION', 'VEGETABLE', '0')");
        rs = statement.executeQuery("SELECT TOP 1 * FROM FOOD ORDER BY FOOD_ID DESC");
        rs.next();
        int newMaxId = rs.getInt("FOOD_ID");
        Assertions.assertNotEquals(maxId,newMaxId);
        Assertions.assertEquals(maxId+1, newMaxId);
    }

    @AfterEach
    @DisplayName("Удаление вставленного элемента")
    public void testDelet() throws SQLException {
        statement.executeUpdate("DELETE FROM FOOD WHERE FOOD_ID=(SELECT MAX(FOOD_ID) FROM FOOD)");
        ResultSet rs = statement.executeQuery("SELECT MAX(FOOD_ID) FROM FOOD");
        rs.next();
        int finalMaxId = rs.getInt(1);
    }

}
