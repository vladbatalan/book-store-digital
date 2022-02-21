package com.javainuse.config.ws;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext){
        MessageDispatcherServlet result = new MessageDispatcherServlet();

        result.setApplicationContext(applicationContext);

        result.setTransformWsdlLocations(true);

        return new ServletRegistrationBean(result, "/api/soap/*");
    }

    @Bean(name = "IdentityProvider")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema providerSchema)
    {
        DefaultWsdl11Definition result = new DefaultWsdl11Definition();

        result.setPortTypeName("IdentityPort");
        result.setLocationUri("/api/soap");
        result.setTargetNamespace("http://com.javainuse/provider");
        result.setSchema(providerSchema);

        return result;
    }

    @Bean
    public XsdSchema calculatorSchema(){
        return new SimpleXsdSchema((new ClassPathResource("identity-provider.xsd")));
    }
}
