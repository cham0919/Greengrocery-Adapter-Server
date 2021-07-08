package com.adapter.api.greengrocery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/greengrocery")
public class GreengroceryController {

    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    private final Greengrocery greengrocery;

    @GetMapping({"", "/"})
    public ResponseEntity<String> fetchList(HttpServletRequest req,
                                              HttpServletResponse res) throws IOException {
        Map<GreengroceryType, List<GreengroceryDto>> vegetableDtos = greengrocery.fetchList();
        return new ResponseEntity<String>(gson.toJson(vegetableDtos), HttpStatus.OK);
    }
}
