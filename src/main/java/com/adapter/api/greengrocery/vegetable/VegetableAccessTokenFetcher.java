package com.adapter.api.greengrocery.vegetable;

import com.adapter.api.token.AccessTokenFetcher;
import com.adapter.api.greengrocery.GreengroceryUris;
import com.adapter.api.common.http.HttpRequest;
import com.adapter.api.common.util.http.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;

@Component
public class VegetableAccessTokenFetcher implements AccessTokenFetcher {

    @Override
    public String fetch() throws IOException {
        HttpResponse resp = fetchAccessToken();
        return extractToken(resp);
    }

    private String extractToken(HttpResponse resp) {
        if (HttpUtils.is2xxSuccessful(resp)) {
            return extractTokenFromHeader(resp);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(resp.getStatusLine().getStatusCode()));
        }
    }

    private String extractTokenFromHeader(HttpResponse resp) {
        return resp.getHeaders("Set-Cookie")[0].getElements()[0].getValue();
    }

    HttpResponse fetchAccessToken() throws IOException {
        return HttpRequest.of()
                .get(URI.create(GreengroceryUris.VegetableUris.TOKEN_URL))
                .execute();
    }
}
