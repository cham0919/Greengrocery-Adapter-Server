package com.adapter.api.greengrocery.vegetable;

import com.adapter.api.common.http.HttpRequest;
import com.adapter.api.common.util.http.HttpUtils;
import com.adapter.api.greengrocery.GreengroceryDto;
import com.adapter.api.greengrocery.GreengroceryFetcher;
import com.adapter.api.greengrocery.GreengroceryType;
import com.adapter.api.greengrocery.GreengroceryUris.VegetableUris;
import com.adapter.api.token.AccessToken;
import com.adapter.api.token.AccessTokenFetcher;
import lombok.RequiredArgsConstructor;
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
public class VegetableFetcher implements GreengroceryFetcher {

    private final AccessTokenFetcher tokenFetcher;

    public VegetableFetcher(VegetableAccessTokenFetcher tokenFetcher) {
        this.tokenFetcher = tokenFetcher;
    }

    @Override
    public GreengroceryDto fetchPrice(String name) throws IOException {
        log.debug("Starting fetch {} price", name);
        if (name == null && name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        HttpResponse resp = fetchVegetablePrice(name);
        log.debug("resp Entity :: {}", resp.getEntity().toString());
        return extractVegetablePrice(resp);
    }

    @Override
    public List<GreengroceryDto> fetchList() throws IOException {
        log.debug("Starting fetch Vegetable List");
        HttpResponse resp = fetchVegetableList();
        log.debug("resp Entity :: {}", resp.getEntity().toString());
        return extractVegetableList(resp);
    }

    @Override
    public GreengroceryType getType() {
        return GreengroceryType.VEGETABLE;
    }

    private GreengroceryDto extractVegetablePrice(HttpResponse resp) throws IOException {
        if (HttpUtils.is2xxSuccessful(resp)) {
            return HttpUtils.extractEntity(resp, GreengroceryDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(resp.getStatusLine().getStatusCode()));
        }
    }

    private List<GreengroceryDto> extractVegetableList(HttpResponse resp) throws IOException {
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

    private HttpResponse fetchVegetablePrice(String name) throws IOException{
        return HttpRequest.of()
                .get(createVegetablePriceURI(name))
                .addHeader(HttpHeaders.AUTHORIZATION, getAccessToken())
                .execute();
    }

    private HttpResponse fetchVegetableList() throws IOException{
        return HttpRequest.of()
                .get(createVegetableListURI())
                .addHeader(HttpHeaders.AUTHORIZATION, AccessToken.getVegetableAccessToken())
                .execute();
    }

    private URI createVegetablePriceURI(String name) {
        return URI.create(VegetableUris.URL + "?name=" + name);
    }

    private URI createVegetableListURI() {
        return URI.create(VegetableUris.URL);
    }

    private String getAccessToken() throws IOException {
        String token = AccessToken.getVegetableAccessToken();
        if (AccessToken.getVegetableAccessToken() == null) {
            token = tokenFetcher.fetch();
        }
        return token;
    }
}
