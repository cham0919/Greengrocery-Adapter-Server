package com.adapter.api.greengrocery.fruit;

import com.adapter.api.token.AccessTokenFetcher;
import com.adapter.api.greengrocery.GreengroceryUris;
import com.adapter.api.common.http.HttpRequest;
import com.adapter.api.common.util.http.HttpUtils;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Component
public class FruitAccessTokenFetcher implements AccessTokenFetcher {

    @Override
    public String fetch() throws IOException{
        HttpResponse resp = fetchAccessToken();
        return extractToken(resp);
    }

    private String extractToken(HttpResponse resp) throws IOException {
        if (HttpUtils.is2xxSuccessful(resp)) {
            Map<String, String> entityMap = HttpUtils.extractEntity(resp, new TypeToken<Map<String, String>>() {}.getType());
            return entityMap.get("accessToken");
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(resp.getStatusLine().getStatusCode()));
        }
    }

    private HttpResponse fetchAccessToken() throws IOException {
        return HttpRequest.of()
                .get(URI.create(GreengroceryUris.FruitUris.TOKEN_URL))
                .execute();
    }
}
