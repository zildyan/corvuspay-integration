## Synopsis

**CorvusPay** is advanced payment gateway with highest security certificate Payment Card Industry Data Security Standard (PCI DSS).

This software enables easy integration of CorvusPay for web shops developed on Java platform.
It's compatible with Java 6 and higher and has only one dependency namely javax.servlet-api

## Installation

**Maven dependency**

```xml
<dependency>
    <groupId>hr.corvuspay</groupId>
    <artifactId>corvuspay-integration</artifactId>
    <version>1.0</version>
    <scope>compile</scope>
</dependency>
```

**Gradle dependency**

## Code Example

**This code example uses Spring as IoC container.**

Pre-requisition is to provide KeyStore name, password and absolute certificate file path to CorvusPayHttpsPostServiceImpl
either by extending and overriding getters or by spring injection of properties on bean declaration

Integration is achieved in two steps: implementation of **service** and **controller**

* Implementation of service that **extends AbstractCorvusPayIntegrationService** and implement all required methods

```java
@Service
public class CorvusPayIntegrationService extends AbstractCorvusPayIntegrationService {

    @Value("${corvuspay.secretkey}") //from property file
    private String secretKey;

    @Value("${corvuspay.storeid}") //from property file
    private String storeId;

    @Autowired
    private CorvusPayHttpsPostService corvusPayHttpsPostService;
    // Injected spring bean declared in spring configuration

    @Override
    public CorvusPayHttpsPostService getCorvusPayHttpsPostService() {
        return corvusPayHttpsPostService;
    } // overridden method to return bean

    @Override
    public String getSecretKey() {
        return secretKey;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public CorvusPayEnvironmentType getEnvironment() {
        return CorvusPayEnvironmentType.TEST;
    }

    @Override
    public String getCurrency() {
        return CorvusPayCountryType.CROATIA.getCurrency();
    }

    @Override
    public String getLanguage() {
        return CorvusPayCountryType.CROATIA.getLanguage();
    }

    @Override
    public CorvusPayRequestData getRequestData(String orderNumber) {
        Order cart = orderService.findOrderById(Long.valueOf(orderNumber)); //mock
        User user = cart.getUser();

        return new CorvusPayRequestData
                .Builder(orderNumber, getSecretKey(), getStoreId(), getLanguage(), getCurrency(), cart.getTotal().toString())
                .cart(getFormattedCartEntries(cart))
                .cardholderName(user.getName())
                .cardholderSurname(user.getSurname())
                .cardholderCity(user.getCity())
                .cardholderAddress(user.getStreet())
                .cardholderZipCode(user.getZipCode())
                .cardholderPhone(user.getPhone())
                .cardholderEmail(user.getEmail())
                .cardholderCountry(CorvusPayCountryType.CROATIA)
                .allCardsAllInstallments()
                .requireCompleteOff()
                .defaultRedirectHash()
                .build();
    }

    private String getFormattedCartEntries(Order order) {
        Map<String, Integer> cartEntries = order.getOrderItems().stream()
                .collect(toMap(OrderItem::getName, OrderItem::getQuantity));
        return formatCartEntries(cartEntries);
    }

    //we set requireCompleteOff in this example so it's a sale and requires refund API call for handling returns
    @Override
    public boolean refundApiCall(String orderNumber) {
        Map<CorvusPayResponseType, String> response = refundApiCallInternal(orderNumber);
        //do something with response
        return true;
    }
}
```

* Controller that **implements CorvusPayIntegrationEndpoints**

```java
@Controller
@RequestMapping(value = "payment/corvuspay")
public class CorvusPayPaymentGatewayController implements CorvusPayIntegrationEndpoints {

    @Autowired
    private CorvusPayIntegrationService corvusPayIntegrationService;

    @Autowired
    private OrderService orderService;

    @Override
    @RequestMapping(value = "/redirect/{orderNumber}", method = RequestMethod.GET)
    public String redirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           @PathVariable(value = "orderNumber") String orderNumber) {

        CorvusPayRequestData requestData = corvusPayIntegrationService.getRequestData(orderNumber);
        requestData.putRequestField(IP, getIP(httpServletRequest));

        httpServletRequest.setAttribute("redirectUrl", corvusPayIntegrationService.getCorvusPayRedirect());
        httpServletRequest.setAttribute("requestData", requestData);

        return corvusPayIntegrationService.getCorvusPaySubmitForm(THYMELEAF);
    }

    @Override
    @RequestMapping(value = "/success")
    public String success(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                          @RequestParam(value = "order_number") String orderNumber,
                          @RequestParam(value = "hash") String hash,
                          @RequestParam(value = "language") String language,
                          @RequestParam(value = "approval_code") String approvalCode) {

        corvusPayIntegrationService.validateTransaction(orderNumber, hash);
        //other checks
        return "checkout/confirmation";
    }

    @Override
    @RequestMapping(value = "/failure")
    public String failure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                          String orderNumber, String language) {
        httpServletRequest.setAttribute("error", "CorvusPay failure");
        return "checkout/error";
    }
}
```

