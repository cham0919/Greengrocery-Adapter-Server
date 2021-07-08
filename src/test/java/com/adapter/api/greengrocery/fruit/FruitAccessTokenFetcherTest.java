package com.adapter.api.greengrocery.fruit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class FruitAccessTokenFetcherTest {

    @InjectMocks
    private FruitAccessTokenFetcher fruitAccessTokenFetcher;

    @Test
    public void fetch_Success_NotNull() throws IOException {
        assertNotNull(fruitAccessTokenFetcher.fetch());
    }

}
