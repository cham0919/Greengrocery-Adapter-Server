package com.adapter.api.greengrocery.fruit;

import com.adapter.api.greengrocery.Greengrocery;
import com.adapter.api.greengrocery.GreengroceryDto;
import com.adapter.api.greengrocery.GreengroceryType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/greengrocery/fruit")
@RequiredArgsConstructor
@Slf4j
public class FruitController {

    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    private final Greengrocery greengrocery;


    @GetMapping({"", "/"})
    public ResponseEntity<String> fetchByName(HttpServletRequest req,
                                            HttpServletResponse res,
                                            @RequestParam String name) throws IOException {
        GreengroceryDto dto = greengrocery.fetchPrice(name, GreengroceryType.FRUIT);
        return new ResponseEntity<String>(gson.toJson(dto), HttpStatus.OK);
    }
}
