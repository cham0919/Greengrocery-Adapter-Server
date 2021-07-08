package com.adapter.api.greengrocery.fruit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.adapter.api.greengrocery.GreengroceryType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;



@ExtendWith(MockitoExtension.class)
public class FruitFetcherTest {

    @InjectMocks
    private FruitFetcher fruitFetcher;
    @Mock
    private FruitAccessTokenFetcher tokenFetcher;

    private final String accseeToken = "6BYNQsD4WZExgjooY4jCzV4AQA4gFIaTm3K5Sxqr5_aG166Y4zXREV_vxg9fwPjA";

    @Test
    public void fetchList_Success_NotNull() throws IOException {
        given(tokenFetcher.fetch()).willReturn(accseeToken);
        assertNotNull(fruitFetcher.fetchList());
    }

    @Test
    public void fetchList_Fail_ResponseStatusException() throws IOException {
        given(tokenFetcher.fetch()).willReturn(null);
        assertThrows(ResponseStatusException.class,
                () -> {fruitFetcher.fetchList();});
    }

    @Test
    public void fetchPrice_Success_NotNull() throws IOException {
        String type = "배";
        given(tokenFetcher.fetch()).willReturn(accseeToken);
        assertNotNull(fruitFetcher.fetchPrice(type));
    }

    @Test
    public void fetchPrice_Fail_ResponseStatusException() throws IOException {
        String type = "배";
        given(tokenFetcher.fetch()).willReturn(null);
        assertThrows(ResponseStatusException.class,
                () -> {fruitFetcher.fetchPrice(type);});
    }

    @Test
    public void fetchPrice_Fail_NullPointerException(){
        assertThrows(NullPointerException.class,
                () -> {fruitFetcher.fetchPrice(null);});
    }

    @Test
    public void getType_Success_Equals() {
        assertEquals(fruitFetcher.getType(), GreengroceryType.FRUIT);
    }
}
