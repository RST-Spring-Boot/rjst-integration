package de.rjst.rjstintegration.config;

import feign.Client;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class FeignConfig extends FeignClientProperties.FeignClientConfiguration {

    @Bean
    public Client feignClient() {
        return new Client.Default(getUnsafeSSLSocketFactory(), (s, sslSession) -> true);
    }

    private javax.net.ssl.SSLSocketFactory getUnsafeSSLSocketFactory() {
        try {
            TrustManager[] trustAllCerts = {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}