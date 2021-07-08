package com.adapter.api.greengrocery;

import java.io.IOException;
import java.util.List;

public interface GreengroceryFetcher {

    GreengroceryDto fetchPrice(String name) throws IOException;

    List<GreengroceryDto> fetchList() throws IOException;

    GreengroceryType getType();

}
