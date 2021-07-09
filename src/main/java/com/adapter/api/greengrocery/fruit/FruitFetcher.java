package com.adapter.api.greengrocery.fruit;

import com.adapter.api.greengrocery.GreengroceryFetcher;
import com.adapter.api.greengrocery.GreengroceryUris.FruitUris;
import com.adapter.api.common.http.HttpRequest;
import com.adapter.api.greengrocery.GreengroceryType;
import com.adapter.api.common.util.http.HttpUtils;
import com.adapter.api.token.AccessToken;
import com.adapter.api.greengrocery.GreengroceryDto;
import com.adapter.api.token.AccessTokenFetcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FruitFetcher implements GreengroceryFetcher {

    private final AccessTokenFetcher tokenFetcher;

    public FruitFetcher(FruitAccessTokenFetcher accessTokenFetcher) {
        this.tokenFetcher = accessTokenFetcher;
    }

    @Override
    public GreengroceryDto fetchPrice(String name) throws IOException {
        log.debug("Starting fetch {} price", name);
        if (name == null && name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        HttpResponse resp = fetchFruitPrice(name);
        log.debug("resp Entity :: {}", resp.getEntity().toString());
        return extractFruitPrice(resp);
    }

    @Override
    public List<GreengroceryDto> fetchList() throws IOException {
        HttpResponse resp = fetchFruitList();
        return extractFruitList(resp);
    }

    @Override
    public GreengroceryType getType() {
        return GreengroceryType.FRUIT;
    }

    private GreengroceryDto extractFruitPrice(HttpResponse resp) throws IOException {
        if (HttpUtils.is2xxSuccessful(resp)) {
            return HttpUtils.extractEntity(resp, GreengroceryDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(resp.getStatusLine().getStatusCode()));
        }
    }

    private List<GreengroceryDto> extractFruitList(HttpResponse resp) throws IOException {
        if (HttpUtils.is2xxSuccessful(resp)) {
            return getGreengroceryDtos(resp);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(resp.getStatusLine().getStatusCode()));
        }
    }

    private List<GreengroceryDto> getGreengroceryDtos(HttpResponse resp) throws IOException {
        List<GreengroceryDto> dtos = new ArrayList<>();
        List<String> list = HttpUtils.extractEntity(resp, List.class);
        list.forEach(name -> {
            dtos.add(new GreengroceryDto().setName(name));});
        return dtos;
    }


    private HttpResponse fetchFruitPrice(String name) throws IOException {
        return HttpRequest.of()
                .get(createFruitPriceURI(name))
                .addHeader(HttpHeaders.AUTHORIZATION, getAccessToken())
                .execute();
    }

    private HttpResponse fetchFruitList() throws IOException {
        return HttpRequest.of()
                .get(createFruitListURI())
                .addHeader(HttpHeaders.AUTHORIZATION, getAccessToken())
                .execute();
    }

    private URI createFruitPriceURI(String name) {
        return URI.create(FruitUris.URL + "?name=" + name);
    }

    private URI createFruitListURI() {
        return URI.create(FruitUris.URL);
    }

    private String getAccessToken() throws IOException {
        String token = AccessToken.getFruitAccessToken();
        if (AccessToken.getFruitAccessToken() == null) {
            token = tokenFetcher.fetch();
            AccessToken.setFruitAccessToken(token);
        }
        return token;
    }
}
