package com.adapter.api.greengrocery;

public class GreengroceryUris {

    public class FruitUris {
        public static final String BASE_URL = "http://fruit.api.postype.net";
        public static final String TOKEN_URL = BASE_URL +"/token";
        public static final String URL = BASE_URL +"/product";
    }

    public class VegetableUris {
        public static final String BASE_URL = "http://vegetable.api.postype.net";
        public static final String TOKEN_URL = BASE_URL +"/token";
        public static final String URL = BASE_URL +"/item";
    }
}
