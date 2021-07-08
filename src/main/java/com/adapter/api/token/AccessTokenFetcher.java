package com.adapter.api.token;

import java.io.IOException;

public interface AccessTokenFetcher{

    String fetch() throws IOException;

}
