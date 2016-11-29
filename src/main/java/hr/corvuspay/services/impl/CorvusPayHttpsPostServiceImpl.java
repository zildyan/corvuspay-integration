package hr.corvuspay.services.impl;

import hr.corvuspay.services.CorvusPayHttpsPostService;
import hr.corvuspay.types.CorvusPayRequestFieldType;
import hr.corvuspay.types.CorvusPayResponseType;
import hr.corvuspay.utils.CorvusPayXmlUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.CorvusPayHttpsPostException;
import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.KeyStoreAndCertificateRequiredDataException;
import static hr.corvuspay.ssl.CorvusPaySslManager.getSslContext;
import static hr.corvuspay.utils.CorvusPayMiscellaneousUtil.isValidData;
import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.getDefaultHostnameVerifier;

public class CorvusPayHttpsPostServiceImpl implements CorvusPayHttpsPostService {

    private String pkcs12Name;
    private String pkcs12Password;

    public CorvusPayHttpsPostServiceImpl() {
    }

    public CorvusPayHttpsPostServiceImpl(String pkcs12Name, String pkcs12Password) {
        this.pkcs12Name = pkcs12Name;
        this.pkcs12Password = pkcs12Password;
    }

    @Override
    public Map<CorvusPayResponseType, String> executeHttpsPost(String url, Map<CorvusPayRequestFieldType, String> requestFields) {

        if (!isValidData(getPkcs12Name(), getPkcs12Password()))
            throw new KeyStoreAndCertificateRequiredDataException();

        SSLContext sslContext = getSslContext(getPkcs12Name(), getPkcs12Password());
        try (CloseableHttpClient httpclient = createClient(sslContext)){
            List<NameValuePair> params = getParams(requestFields);
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpclient.execute(post);

            //todo Parsati response sa XmlUtil - parsanje ne radi jer je napisano napamet.
            String result = readResponse(response);
            return null;
        } catch (IOException e) {
            throw new CorvusPayHttpsPostException(e);
        }
    }

    public static CloseableHttpClient createClient(SSLContext sslContext) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                sslContext.getSocketFactory(),
                getDefaultHostnameVerifier());

        httpClientBuilder.setSSLSocketFactory(sslConnectionFactory);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                .build();

        httpClientBuilder.setConnectionManager(new BasicHttpClientConnectionManager(registry));
        return httpClientBuilder.build();
    }

    private List<NameValuePair> getParams(Map<CorvusPayRequestFieldType, String> requestFields) {
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<CorvusPayRequestFieldType, String> entry : requestFields.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey().name().toLowerCase(), entry.getValue()));
        }
        return params;
    }

    private String readResponse(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public String getPkcs12Name() {
        return pkcs12Name;
    }

    public void setPkcs12Name(String pkcs12Name) {
        this.pkcs12Name = pkcs12Name;
    }

    public String getPkcs12Password() {
        return pkcs12Password;
    }

    public void setPkcs12Password(String pkcs12Password) {
        this.pkcs12Password = pkcs12Password;
    }
}
