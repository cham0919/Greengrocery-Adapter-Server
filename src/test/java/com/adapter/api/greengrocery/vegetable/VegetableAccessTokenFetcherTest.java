package com.adapter.api.greengrocery.vegetable;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;



@ExtendWith(MockitoExtension.class)
public class VegetableAccessTokenFetcherTest {

    @InjectMocks
    private VegetableAccessTokenFetcher vegetableAccessTokenFetcher;

    @Test
    public void fetch_Success_NotNull() throws IOException {
        assertNotNull(vegetableAccessTokenFetcher.fetch());
    }
}
