package hr.corvuspay.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CorvusPayIntegrationEndpoints {

    String redirect(HttpServletRequest request,  HttpServletResponse response, String orderNumber);

    String success(HttpServletRequest request,  HttpServletResponse response, String orderNumber, String hashCode,
                   String language, String approvalCode);

    String failure(HttpServletRequest request,  HttpServletResponse response, String orderNumber, String language);

//    ako je isti endpoint na koji corvus pay vraca usera tada dodatna polja accountId i subscriptionExpDate
//    trebaju biti dodana success metodi - u springu je moguće postaviti option RequestParam required=false
//    String successWithSubscription(HttpServletRequest request,  HttpServletResponse response, String orderNumber, String hashCode,
//                                   String language, String approvalCode, String accountId, String subscriptionExpDate);

}
