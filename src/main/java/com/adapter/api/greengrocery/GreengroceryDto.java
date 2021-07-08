package com.adapter.api.greengrocery;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class GreengroceryDto {

    private String name;
    private String price;

}
