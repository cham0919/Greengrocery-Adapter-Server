package com.adapter.api.greengrocery;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Greengrocery {

    GreengroceryDto fetchPrice(String name, GreengroceryType type) throws IOException;

    Map<GreengroceryType, List<GreengroceryDto>> fetchList() throws IOException;

    List<GreengroceryDto> fetchList(GreengroceryType type) throws IOException;

}
