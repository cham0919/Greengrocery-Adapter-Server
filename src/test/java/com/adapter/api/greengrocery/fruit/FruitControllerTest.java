package com.adapter.api.greengrocery.fruit;

import com.adapter.api.greengrocery.Greengrocery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FruitController.class)
public class FruitControllerTest {

    @Autowired
    protected MockMvc mvc;
    @MockBean
    private Greengrocery greengrocery;

    @Test
    public void fetchByName_Success_200() throws Exception {
         mvc.perform(
                get("/greengrocery/fruit?name=배"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void fetchByName_Fail_400() throws Exception {
         mvc.perform(
                get("/greengrocery/fruit"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
