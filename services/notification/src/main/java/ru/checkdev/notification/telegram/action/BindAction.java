package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.Calendar;

/**
 * 3. Мидл
 * Класс реализует пункт меню привязки аккаунта telegram к платформе CheckDev
 *
 * @author Olga Ilyina, user Olga
 * @since 16.04.2025
 */
@AllArgsConstructor
@Slf4j
public class BindAction implements Action {
    private static final String ERROR_OBJECT = "error";
    private static final String URL_AUTH_REGISTRATION = "/registration";
    private final TgConfig tgConfig = new TgConfig("tg/", 8);
    private final TgAuthCallWebClint authCallWebClint;
    private final String urlSiteAuth;
    private String login;
    private String password;

    public BindAction(TgAuthCallWebClint authCallWebClint, String urlSiteAuth) {
        this.authCallWebClint = authCallWebClint;
        this.urlSiteAuth = urlSiteAuth;
    }

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId().toString();
        var text = "Введите последовательно через пробел login и password:";
        return new SendMessage(chatId, text);
    }

    /**
     * Метод формирует ответ пользователю.
     * Весь метод разбит на 4 этапа проверки.
     * 1. Проверка на соответствие формату Email введенного текста.
     * 2. Отправка данных в сервис Auth и если сервис не доступен сообщаем
     * 3. Если сервис доступен, получаем от него ответ и обрабатываем его.
     * 3.1 ответ при ошибке.
     * 3.2 ответ при успешной обработке.
     *
     * @param message Message
     * @return BotApiMethod<Message>
     */
    @Override
    public BotApiMethod<Message> callback(Message message) {
        var chatId = message.getChatId().toString();
        var msg = message.getText();
        var text = "";
        var sl = System.lineSeparator();

        String[] s = msg.split(" ");
        if (s.length < 2) {
            text = "Введите снова логин и пароль." + sl
                    + "или попробуйте снова." + sl
                    + "/bind";
            login = null;
            password = null;
            return new SendMessage(chatId, text);
        }
        login = s[0];
        password = s[1];

        var person = new PersonDTO(null, password, true, null,
                Calendar.getInstance(), login);
        Object result;
        try {
            result = authCallWebClint.doPost(URL_AUTH_REGISTRATION, person).block();
        } catch (Exception e) {
            log.error("WebClient doPost error: {}", e.getMessage());
            text = "Сервис не доступен попробуйте позже" + sl
                    + "/start";
            return new SendMessage(chatId, text);
        }

        var mapObject = tgConfig.getObjectToMap(result);

        if (mapObject.containsKey(ERROR_OBJECT)) {
            text = "Ошибка связывания аккаунта с платформой CheckDev: " + mapObject.get(ERROR_OBJECT);
            return new SendMessage(chatId, text);
        }

        text = "Ваш аккаунт привязан к платформе CheckDev: " + sl
                + "Логин: " + login + sl
                + "Пароль: " + password + sl
                + urlSiteAuth;
        return new SendMessage(chatId, text);
    }
}