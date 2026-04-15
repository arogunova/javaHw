import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hofftech.service.ParcelService;
import ru.hofftech.telegram.TruckBot;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckBotTest {

    private TruckBot bot;

    @BeforeEach
    void setUp() {
        // Создаём реальный репозиторий для тестов
        // Для тестов используем in-memory или реальную БД
        // Проще всего создать через конструктор с репозиторием
        ParcelService parcelService = new ParcelService(null); // Временно
        bot = new TruckBot(parcelService);

        // Внедряем сервис через рефлексию
        try {
            java.lang.reflect.Field field = TruckBot.class.getDeclaredField("parcelService");
            field.setAccessible(true);
            field.set(bot, parcelService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Команда /find-all должна вернуть ответ")
    void testFindAllCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/find-all");

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Команда /find должна вернуть ответ")
    void testFindCommandExists() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/find test");

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Команда /create должна вернуть ответ")
    void testCreateCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/create -name Тестовая -form X");

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Команда /delete должна вернуть ответ")
    void testDeleteCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/delete Тестовая");

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Команда /load должна вернуть ответ")
    void testLoadCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/load -parcels test1\\ntest2 -type maxdense");

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Неизвестная команда должна вернуть подсказку")
    void testUnknownCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/unknown");

        assertThat(response).contains("Неизвестная команда");
    }
}