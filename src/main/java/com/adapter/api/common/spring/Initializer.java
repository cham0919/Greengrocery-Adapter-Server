package com.adapter.api.common.spring;

import com.adapter.api.token.AccessTokenInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final AccessTokenInitializer token;


    @Override
    public void run(String... args) throws Exception {
        token.init();
    }
}
