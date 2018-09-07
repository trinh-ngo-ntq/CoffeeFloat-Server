package com.login;

import javax.servlet.Filter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.login.filter.ApiFilter;
import jp.co.yahoo.yconnect.YConnectExplicit;

@SpringBootApplication
public class SpringLoginApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringLoginApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringLoginApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    YConnectExplicit initYConnectExplicit() {
        return new YConnectExplicit();
    }

    @Bean
    public FilterRegistrationBean barFilter() {
        return createFilterRegistration(new ApiFilter());
    }

    private FilterRegistrationBean createFilterRegistration(Filter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns("/1807/api/*");
        return registration;
    }
}
