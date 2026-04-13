
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hofftech.repository.ParcelRepository;
import ru.hofftech.service.ParcelService;
import ru.hofftech.telegram.TruckBot;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckBotTest {

    private TruckBot bot;

    @BeforeEach
    void setUp() {
        ParcelRepository repository = new ParcelRepository();
        ParcelService parcelService = new ParcelService(repository);
        bot = new TruckBot("test_token", "test_bot");

        try {
            java.lang.reflect.Field field = TruckBot.class.getDeclaredField("parcelService");
            field.setAccessible(true);
            field.set(bot, parcelService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Команда /find-all должна вернуть список посылок")
    void testFindAllCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/find-all");

        assertThat(response).contains("Всего посылок: 9");
        assertThat(response).contains("Посылка_тип_1");
    }

    @Test
    @DisplayName("Команда /find с существующим именем должна вернуть посылку")
    void testFindCommandExists() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/find Посылка_тип_1");

        assertThat(response).contains("Посылка: Посылка_тип_1");
        assertThat(response).contains("Символ: 1");
    }

    @Test
    @DisplayName("Команда /create должна создать новую посылку")
    void testCreateCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/create -name Тестовая -form X");

        assertThat(response).contains("✅ Посылка 'Тестовая' создана");

        String findAll = (String) method.invoke(bot, "/find-all");
        assertThat(findAll).contains("Тестовая");
    }

    @Test
    @DisplayName("Команда /delete должна удалить посылку")
    void testDeleteCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        method.invoke(bot, "/create -name Для_удаления -form X ");

        String beforeDelete = (String) method.invoke(bot, "/find-all");
        assertThat(beforeDelete).contains("Для_удаления");

        String response = (String) method.invoke(bot, "/delete Для_удаления");
        assertThat(response).contains("✅ Посылка 'Для_удаления' удалена");

        String afterDelete = (String) method.invoke(bot, "/find-all");
        assertThat(afterDelete).doesNotContain("Для_удаления");
    }

    @Test
    @DisplayName("Команда /load должна упаковать посылки")
    void testLoadCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        method.invoke(bot, "/create -name ТестКуб -form XXX\\nXXX\\nXXX");

        String response = (String) method.invoke(bot, "/load -parcels Посылка_тип_1\\nТестКуб -type maxdense");

        assertThat(response).contains("Результат упаковки");
        assertThat(response).contains("Использовано машин");
    }

    @Test
    @DisplayName("Неизвестная команда должна вернуть подсказку")
    void testUnknownCommand() throws Exception {
        Method method = TruckBot.class.getDeclaredMethod("processCommand", String.class);
        method.setAccessible(true);

        String response = (String) method.invoke(bot, "/unknown");

        System.out.println("DEBUG response: " + response);  // ← добавить

        assertThat(response).contains("Неизвестная команда");
        assertThat(response).contains("/find-all");
        assertThat(response).contains("/load");
    }
}