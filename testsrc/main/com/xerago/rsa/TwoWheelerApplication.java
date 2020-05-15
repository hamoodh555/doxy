package com.xerago.rsa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.valuemomentum.IfoundryConfig;


@SpringBootApplication
@ComponentScan({ "com.xerago.rsa", "com.xerago.rsa.communication" })
@Import({ IfoundryConfig.class })
@EnableTransactionManagement
@EnableJpaRepositories
public class TwoWheelerApplication {

	private static final Logger LOGGER = LogManager.getRootLogger();

	public static void main(String[] args) {
		SpringApplication.run(TwoWheelerApplication.class, args);
	}

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactoryBean() {
		LOGGER.info("HibernateJpaSessionFactoryBean instantiated");
		return new HibernateJpaSessionFactoryBean();
	}

	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.indentOutput(true);
		builder.serializationInclusion(Include.NON_NULL);
		builder.configure(objectMapperWithoutRoot());
		return builder;
	}
	
	@Primary
	@Bean(name = "ObjectMapperWithoutRoot")
	public ObjectMapper objectMapperWithoutRoot() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	@Bean(name = "ObjectMapperWithRoot")
	public ObjectMapper objectMapperWithRoot() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	@Bean
	public XmlMapper xmlMapper() {
		JacksonXmlModule jacksonXmlModule = new JacksonXmlModule();
		XmlMapper xmlMapper = new XmlMapper(jacksonXmlModule);
		xmlMapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		xmlMapper.setSerializationInclusion(Include.NON_NULL);
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
		xmlMapper.setDefaultUseWrapper(false);
		return xmlMapper;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();

		// set up a buffering request factory, so response body is always
		// buffered
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(
				requestFactory);
		requestFactory.setOutputStreaming(false);
		restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);

		// Logging interceptors
		restTemplate.getInterceptors().add(new LoggingRequestInterceptor());

		LOGGER.info("restTemplate.getInterceptors().size() :::" + restTemplate.getInterceptors().size());
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
		httpMessageConverter.setObjectMapper(objectMapper);
		restTemplate.getMessageConverters().add(httpMessageConverter);
		return restTemplate;
	}

	@Bean
	public javax.validation.Validator localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}

	@Bean(name = "errorcodeMessages")
	public static PropertiesFactoryBean mapper() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource("twowheelererrorcode.properties"));
		return bean;
	}
	
}
