package ru.checkdev.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Slf4j
public class OAuthCall {

    private static final int RETRIES = 3;
    private static final long DELAY = 1000;

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    public String doGet(Principal user, String url) throws Exception {
        return this.retryCall(user, "GET", url, null);
    }

    public String doPost(Principal user, String url, String data) throws Exception {
        return this.retryCall(user, "POST", url, data);
    }

    public interface Act<T> {
        T exec() throws Exception;
    }

    public <R> R exec(Act<R> act, R defVal) {
        int i = 0;
        do {
            i++;
            try {
                return act.exec();
            } catch (Exception e) {
                log.error("Attempt {} failed: {}", i, e.getMessage(), e);
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted during sleep", ie);
                }
            }
        } while (i < RETRIES);
        return defVal;
    }

    private String call(Principal user, String method, String url, String data) throws Exception {
        String access = user != null ? ((Map<String, String>) ((Map<String, Object>) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails())
                .get("details")).get("tokenValue") : null;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.addRequestProperty("User-Agent", "Mozilla/5.0");
        con.addRequestProperty("Authorization", "Bearer " + access);
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("charset", "utf-8");
        con.setUseCaches(false);
        if (data != null) {
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(data.getBytes(StandardCharsets.UTF_8));
            }
        }
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            in.lines().forEach(response::append);
        }
        return response.toString();
    }

    private String retryCall(Principal user, String method, String url, String data) {
        Act<String> permitExc = () -> {
            return call(user, method, url, data);
        };
        var result = exec(permitExc, "Запрос не был отработан корректно");
        log.info(result);
        return result;
    }
}

