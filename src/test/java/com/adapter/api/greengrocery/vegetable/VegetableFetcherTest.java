package com.adapter.api.greengrocery.vegetable;

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
public class VegetableFetcherTest {

    @InjectMocks
    private VegetableFetcher vegetableFetcher;
    @Mock
    private VegetableAccessTokenFetcher tokenFetcher;

    private final String accseeToken = "WjLULbBnvngmXQiAuDEQcnFJGfOvWGtBJ4sYWi9xyKJVUQGuQiIhdgKAZdM3_uIW";

    @Test
    public void fetchList_Success_NotNull() throws IOException {
        given(tokenFetcher.fetch()).willReturn(accseeToken);
        assertNotNull(vegetableFetcher.fetchList());
    }

    @Test
    public void fetchList_Fail_ResponseStatusException() throws IOException {
        given(tokenFetcher.fetch()).willReturn(null);
        assertThrows(ResponseStatusException.class,
                () -> {vegetableFetcher.fetchList();});
    }

    @Test
    public void fetchPrice_Success_NotNull() throws IOException {
        String type = "상추";
        given(tokenFetcher.fetch()).willReturn(accseeToken);
        assertNotNull(vegetableFetcher.fetchPrice(type));
    }

    @Test
    public void fetchPrice_Fail_ResponseStatusException() throws IOException {
        String type = "상추";
        given(tokenFetcher.fetch()).willReturn(null);
        assertThrows(ResponseStatusException.class,
                () -> {vegetableFetcher.fetchPrice(type);});
    }

    @Test
    public void fetchPrice_Fail_NullPointerException(){
        assertThrows(NullPointerException.class,
                () -> {vegetableFetcher.fetchPrice(null);});
    }

    @Test
    public void getType_Success_Equals() {
        assertEquals(vegetableFetcher.getType(), GreengroceryType.VEGETABLE);
    }
}
