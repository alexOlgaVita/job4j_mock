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
public class UnbindAction implements Action {
    private static final String ERROR_OBJECT = "error";
    private static final String URL_AUTH_UNBIND = "/auth/deleteByLoginPassword/{login}/{password}";
    private final TgConfig tgConfig = new TgConfig("tg/", 8);
    private final TgAuthCallWebClint authCallWebClint;
    private final String urlSiteAuth;
    private String login;
    private String password;

    public UnbindAction(TgAuthCallWebClint authCallWebClint, String urlSiteAuth) {
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
                    + "/unbind";
            login = null;
            password = null;
            return new SendMessage(chatId, text);
        }
        login = s[0];
        password = s[1];

        try {
            authCallWebClint.doDelete(URL_AUTH_UNBIND
                    .replace("{login}", login)
                    .replace("{password}", password)).block();
        } catch (Exception e) {
            log.error("WebClient doDelete error: {}", e.getMessage());
            text = "Сервис не доступен попробуйте позже" + sl
                    + "/start";
            return new SendMessage(chatId, text);
        }

        text = "Ваш аккаунт отвязан от платформы CheckDev: " + sl
                + "Логин: " + login + sl
                + "Пароль: " + password + sl
                + urlSiteAuth;
        return new SendMessage(chatId, text);
    }
}