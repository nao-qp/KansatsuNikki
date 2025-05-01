package plant.spring.application.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonParseService {
	@Autowired
    private ObjectMapper objectMapper;

    public List<String> parseJsonList(String json, String keyForErrorMessage) throws IOException {
        if (!StringUtils.hasText(json)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new IOException(keyForErrorMessage + "のJSON解析に失敗しました。", e);
        }
    }
}
