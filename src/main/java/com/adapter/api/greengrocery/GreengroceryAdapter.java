package com.adapter.api.greengrocery;

import com.adapter.api.token.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GreengroceryAdapter implements Greengrocery {

    private final Map<GreengroceryType,GreengroceryFetcher> fetcherMap = new HashMap<>();

    public GreengroceryAdapter(List<GreengroceryFetcher> fetcherList) {
        fetcherList.forEach(fetcher -> this.fetcherMap.put(fetcher.getType(), fetcher));
    }

    @Override
    public GreengroceryDto fetchPrice(String name, GreengroceryType type) throws IOException {
        GreengroceryFetcher fetcher = fetcherMap.get(type);
        return fetcher.fetchPrice(name);
    }

    @Override
    public Map<GreengroceryType, List<GreengroceryDto>> fetchList() throws IOException {
        Map<GreengroceryType, List<GreengroceryDto>> greengroceryListMap = new HashMap<>();
        for (Map.Entry<GreengroceryType, GreengroceryFetcher> entry : fetcherMap.entrySet()) {
            greengroceryListMap.put(entry.getKey(), fetchList(entry.getKey()));
        }
        return greengroceryListMap;
    }

    @Override
    public List<GreengroceryDto> fetchList(GreengroceryType type) throws IOException {
        GreengroceryFetcher fetcher = fetcherMap.get(type);
        return fetcher.fetchList();
    }
}
