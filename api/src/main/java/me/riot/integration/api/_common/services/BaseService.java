package me.riot.integration.api._common.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.riot.integration.api._common.datamodel.BaseDTO;
import me.riot.integration.api._common.datamodel.BaseOrmBean;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.AccountDTO;
import me.riot.integration.api.account.rest.orm.AccountOrmBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;


@Component
public abstract class BaseService<Orm extends BaseOrmBean, DTO extends BaseDTO> {
    private static final String API_KEY = "X-Riot-Token";
    protected static final String _BASE_END_POINT = "riot/";
    @Value("${api.integration.url}")
    protected String _apiUrl;
    @Value("${api.integration.url_eun1}")
    protected String _apiUrlEun1;
    @Value("${api.integration.key}")
    protected String _apiKey;
    @Value("${api.datadragon}")
    protected String _dataDragonUrl;

    @Autowired
    protected ObjectMapper _objectMapper;

    protected abstract JpaRepository getRepository();


    protected String sendRequest(String address, HTTPMethod method) {
        URL url;
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            url = URI.create(address).toURL();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.toString());

            connection.setRequestProperty(API_KEY, _apiKey);

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content.toString();
    }

    public byte[] sendRequestBytes(String address, HTTPMethod method) {
        URL url;
        HttpURLConnection connection = null;

        try {
            url = URI.create(address).toURL();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.toString());

            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Transactional
    protected void save(DTO dto) {
        Orm orm = this.build(dto);
        this.getRepository().save(orm);
    }

    protected abstract Orm build(DTO dto);

}
