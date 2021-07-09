# Greengrocery-Adapter-Server

청과물 중계 API로 청과물에 대한 목록과 가격을 조회할 수 있습니다.

## 설정

 - ```application.properties``` 의 port 설정 바랍니다. 
기본 설정은 8889입니다.
 - ```index.html``` 로 테스트 가능합니다.
 
 ![image](https://user-images.githubusercontent.com/61372486/125101780-8fea6c80-e115-11eb-9685-52efa806d81a.png)


<br/><br/>

## API DOC

 ### 1. 리스트 조회
 
 - url : ```/greengrocery```
 - http method : ```GET```

<br/>
 
 ### 2. 가격 조회
 

 #### 과일 조회
 
 - url : ```/greengrocery/fruit```
 - http method : ```GET```
 - param : ```name```  

<br/>

  #### 야채 조회
 
 - url : ```/greengrocery/vegetable```
 - http method : ```GET```
 - param : ```name```

<br/><br/>

## 기능 구현


 
 ### 1. AccessToken 취득
 
 #### UML
 
 ![image](https://user-images.githubusercontent.com/61372486/125107164-6df3e880-e11b-11eb-9442-a536a8a6d4ff.png)
 
 
 - ```AccessToken``` : AccessToken을 저장하는 Dto입니다. 실 Token값이 저장됩니다.
 
 - ```AccessTokenFetcher``` : Token을 가져오는 ```fetch()```를 선언한 interface 입니다.
 
 - ```VegetableFetcher```, ```FruitAccessTokenFetcher``` : ```AccessTokenFetcher```를 상속받아 Token을 가져오는 실 기능을 구현합니다.
 
 - ```Initializable``` : 초기화를 담당하는 ```init()```가 선언된 interface 입니다.
 
 - ```AccessTokenInitializer``` : AccessToken을 init하는 기능을 구현합니다.
 
 <br/><br/>
 
 ### 구현 
 
 **AccessToken**
 
 ```java
public class AccessToken {

    @Getter @Setter
    private static String fruitAccessToken;
    @Getter @Setter
    private static String vegetableAccessToken;

}
```

 - Token을 저장하는 Class입니다.
 - Token은 static으로 선언되어 전역적으로 사용할 수 있습니다.


 <br/>
 
  **FruitAccessTokenFetcher**
  
  ```java
@Service
public class FruitAccessTokenFetcher implements AccessTokenFetcher {

    @Override
    public String fetch() throws IOException{
        HttpResponse resp = fetchAccessToken();
        return extractToken(resp);
    }

    private String extractToken(HttpResponse resp) throws IOException {
        if (HttpUtils.is2xxSuccessful(resp)) {
            Map<String, String> entityMap = HttpUtils.extractEntity(resp, Map.class);
            return entityMap.get(ACCESSTOKEN.getToken());
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(resp.getStatusLine().getStatusCode()));
        }
    }

    private HttpResponse fetchAccessToken() throws IOException {
        return HttpRequest.of()
                .get(URI.create(GreengroceryUris.FruitUris.TOKEN_URL))
                .execute();
    }
}
 ```

 - 실 Token을 가져오는 기능을 구현한 Class 입니다.
 - ```VegetableAccessTokenFetcher```로 동일합니다.
  

 <br/>
 
 **AccessTokenInitializer**
 
 ```java
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
```
 - Token을 모두 가져와 ```AccessToken```에 삽입합니다.
 
  ※ ```setAccessToken```은 Fetcher Class명 기반으로 ```AccessToken```의 set Method를 찾아 값을 삽입합니다. 차후 목록이 추가되었을 때, class명과 변수명을 일치시켜야합니다.
  <br/>
   예) (```VegetableAccessTokenFetcher```,  ```VegetableAccessToken```), (```FruitAccessTokenFetcher```, ```FruitAccessToken```)
   
   
   <br/>
   
   **Initializer**
   
   ```java
@Configuration
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final AccessTokenInitializer token;

    @Override
    public void run(String... args) {
        token.init();
    }

```  

 - AccessToken은 재사용이 가능하기에 어플리케이션 기동될 때 초기화 작업을 거칩니다.
 - 정상적으로 Token 값을 가져왔다면, 저장 후 재사용됩니다.
 
 <br/><br/>
 
  ### 2. 청과물 정보 취득
  
  #### UML
   
   ![image](https://user-images.githubusercontent.com/61372486/125107063-5157b080-e11b-11eb-84cf-43f3728a7e34.png)
   
   
   - ```GreengroceryFetcher``` : 청과물에 대한 정보를 취득하는 method가 정의되어있는 interface입니다. 청과물 목록 가져오기, 청과물 가격 가져오기가 선언되어 있습니다.
   
   - ```VegetableFetcher```, ```FruitFetcher``` : ```GreengroceryFetcher```을 상속받아 실 정보를 취득하는 기능이 구현된 Class입니다.
   
   - ```Greengrocery``` : 클라이언트가 사용할 adapter interface입니다. 
   
   - ```GreengroceryAdapter``` : ```Greengrocery```를 상속받아 실 기능들이 구현되어있습니다. 
   
 
   
   <br/><br/>
   
   ### 구현 

   **GreengroceryFetcher**
   
   ```java
public interface GreengroceryFetcher {

    GreengroceryDto fetchPrice(String name) throws IOException;

    List<GreengroceryDto> fetchList() throws IOException;

    GreengroceryType getType();

}
```

  - 각 기능들이 선언되어있습니다.
  - 가격 조회, 목록 조회, Type 가져오기가 있습니다.
  - Type 가져오기에 대한 기능은 하단의 **GreengroceryAdapter**를 참조해주세요
  
  <br/><br/>
  
  **VegetableAccessTokenFetcher**
  
  ```java
    @Override
    public GreengroceryDto fetchPrice(String name) throws IOException {
        log.debug("Starting fetch {} price", name);
        if (name == null && name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        HttpResponse resp = fetchFruitPrice(name);
        log.debug("resp Entity :: {}", resp.getEntity().toString());
        return extractFruitPrice(resp);
    }

    @Override
    public List<GreengroceryDto> fetchList() throws IOException {
        HttpResponse resp = fetchFruitList();
        return extractFruitList(resp);
    }

    ...

      private String getAccessToken() throws IOException {
            String token = AccessToken.getFruitAccessToken();
            if (AccessToken.getFruitAccessToken() == null) {
                token = tokenFetcher.fetch();
                AccessToken.setFruitAccessToken(token);
            }
            return token;
        }
```
   - name 값을 통한 가격 정보 가져오기와 목록 가져오기 기능이 구현되어 있습니다.
   
   - 초기 기동 시 Token값을 가져오지 못했을 때를 대비해 요청 보내기 전 Token값이 없다면 한번 더 취득합니다.
   
   
   <br/><br/>
   

  **Greengrocery**
  
  ```java
public interface Greengrocery {

    GreengroceryDto fetchPrice(String name, GreengroceryType type) throws IOException;

    Map<GreengroceryType, List<GreengroceryDto>> fetchList() throws IOException;

    List<GreengroceryDto> fetchList(GreengroceryType type) throws IOException;

}
```

 - View Layer에서 사용할 interface입니다.
 
 <br/><br/>
 
 **GreengroceryAdapter**
 
 ```java
public class GreengroceryAdapter implements Greengrocery {

    private final Map<GreengroceryType,GreengroceryFetcher> fetcherMap = new HashMap<>();

    public GreengroceryAdapter(List<GreengroceryFetcher> fetcherList) {
        fetcherList.forEach(fetcher -> this.fetcherMap.put(fetcher.getType(), fetcher));
    }

    @Override
    public GreengroceryDto fetchPrice(String name, GreengroceryType type) throws IOException {
        GreengroceryFetcher fetcher = fetcherMap.get(type);
        return fetcher.fetchPrice(name);
    }

    @Override
    public Map<GreengroceryType, List<GreengroceryDto>> fetchList() throws IOException {
        Map<GreengroceryType, List<GreengroceryDto>> greengroceryListMap = new HashMap<>();
        for (Map.Entry<GreengroceryType, GreengroceryFetcher> entry : fetcherMap.entrySet()) {
            greengroceryListMap.put(entry.getKey(), fetchList(entry.getKey()));
        }
        return greengroceryListMap;
    }

    @Override
    public List<GreengroceryDto> fetchList(GreengroceryType type) throws IOException {
        GreengroceryFetcher fetcher = fetcherMap.get(type);
        return fetcher.fetchList();
    }
}
```
 
 - ```GreengroceryFetcher```의 객체를 미리 Map으로 저장하고 있다 요청이 들어왔을 시, 필요한 객체를 얻어와 기능을 동작시킵니다.
 - 이때 key값을 Enum Type으로 지정합니다. 
 
    <br/><br/>
    
    **FruitController**
    
```java
    @GetMapping({"", "/"})
    public ResponseEntity<String> fetchByName(HttpServletRequest req,
                                            HttpServletResponse res,
                                            @RequestParam String name) throws IOException {
        GreengroceryDto dto = greengrocery.fetchPrice(name, GreengroceryType.FRUIT);
        return new ResponseEntity<String>(gson.toJson(dto), HttpStatus.OK);
    }

```

 - Controller입니다. 
 - 에러처리는 Advice를 사용합니다.
 
 <br/><br/>
 
 **GreengroceryAdvice**
 
 ```java
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> IOException(IOException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<String>(HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> ResponseStatusException(ResponseStatusException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<String>(HttpStatus.BAD_GATEWAY);
    }
```

 - 에러발생 시, log와 함께 에러 코드를 return합니다.