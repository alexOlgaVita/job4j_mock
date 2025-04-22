package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

/**
 * 3. Мидл
 * Класс реализует пункт меню выдачи ФИО и почты, привязанные к этому аккаунту
 *
 * @author Olga Ilyina, user Olga
 * @since 16.04.2025
 */
@AllArgsConstructor
@Slf4j
public class InfoUserAction implements Action {
    private static final String ERROR_OBJECT = "error";
    private static final String URL_PERSON_PROFILE = "/auth/userInfo/{email}";
    private final TgConfig tgConfig = new TgConfig("tg/", 8);
    private final TgAuthCallWebClint authCallWebClint;
    private final String urlSiteAuth;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId().toString();
        var text = "Введите email чтобы узнать ФИО и email для данного аккаунта:";
        return new SendMessage(chatId, text);
    }

    /**
     * Метод формирует ответ пользователю.
     * Весь метод разбит на 3 этапа проверки.
     * 1. Отправка запроса в сервис Auth и если сервис не доступен сообщаем
     * 2. Если сервис доступен, получаем от него ответ и обрабатываем его.
     * 2.1 ответ при ошибке получения данных
     * 2.2 ответ при успешном получении данных.
     *
     * @param message Message
     * @return BotApiMethod<Message>
     */
    @Override
    public BotApiMethod<Message> callback(Message message) {
        var chatId = message.getChatId().toString();
        var email = message.getText();
        var text = "";
        var sl = System.lineSeparator();

        if (!tgConfig.isEmail(email)) {
            text = "Email: " + email + " не корректный." + sl
                    + "попробуйте снова." + sl
                    + "/check";
            return new SendMessage(chatId, text);
        }

        var password = tgConfig.getPassword();

        Object result;
        try {
            result = authCallWebClint.doGet(URL_PERSON_PROFILE.replace("{email}", email)).block();
        } catch (Exception e) {
            log.error("WebClient doGet error: {}", e.getMessage());
            text = "Сервис не доступен попробуйте позже" + sl
                    + "/start";
            return new SendMessage(chatId, text);
        }

        System.out.println("result = " + result);
        var mapObject = tgConfig.getObjectToMap(result);
        System.out.println("mapObject = " + mapObject);

        if (mapObject.containsKey(ERROR_OBJECT)) {
            text = "Ошибка получения данных: " + mapObject.get(ERROR_OBJECT);
            return new SendMessage(chatId, text);
        }

        text = "У текущего аккаунта: " + sl
                + "ФИО: " + mapObject.get("username") + sl
                + "Почта: " + mapObject.get("email") + sl
                + urlSiteAuth;
        System.out.println("mapObject.get('email') = " + mapObject.get("email"));
        return new SendMessage(chatId, text);
    }
}
