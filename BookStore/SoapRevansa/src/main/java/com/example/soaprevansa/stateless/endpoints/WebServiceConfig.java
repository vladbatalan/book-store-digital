package com.example.soaprevansa.stateless.endpoints;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

        return new ServletRegistrationBean(result, "/sample/*");
    }

    @Bean(name = "Calculator")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema calculatorSchema)
    {
        DefaultWsdl11Definition result = new DefaultWsdl11Definition();

        result.setPortTypeName("CalculatorPort");
        result.setLocationUri("/sample");
        result.setTargetNamespace("http://com.example.soaprevansa.stateless/Calculator");
        result.setSchema(calculatorSchema);

        return result;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public XsdSchema calculatorSchema(){
        return new SimpleXsdSchema((new ClassPathResource("Calculator.xsd")));
    }
}
