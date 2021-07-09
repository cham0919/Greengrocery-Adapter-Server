package com.adapter.api.token;

import com.adapter.api.lang.Initializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AccessTokenInitializer implements Initializable {

    private final List<AccessTokenFetcher> fetcherList = new ArrayList<>();

    public AccessTokenInitializer(List<AccessTokenFetcher> fetcherList) {
        fetcherList.forEach(this.fetcherList::add);
    }

    @Override
    public void init() {
        log.debug("Starting AccessToken Initialization");
        fetcherList.forEach(this::saveToken);
    }

    public void saveToken(AccessTokenFetcher tokenFetcher){
        try {
            String accessToken = tokenFetcher.fetch();
            setAccessToken(tokenFetcher, accessToken);
            log.debug("{} Result :: {}",tokenFetcher.getClass().getSimpleName(), accessToken);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }

    private void setAccessToken(AccessTokenFetcher tokenFetcher, String accessToken) throws Throwable {
        String fetchClazzName = tokenFetcher.getClass().getSimpleName();
        String tokenParamName = fetchClazzName.replace("Fetcher", "");
        Method setMethod = AccessToken.class.getMethod("set" + tokenParamName, String.class);
        setMethod.invoke(AccessToken.class, accessToken);
    }
}
