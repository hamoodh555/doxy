package com.xerago.rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
		ClientHttpResponse clientHttpResponse  = null;
		try {
			traceRequest(request, body);
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			clientHttpResponse = execution.execute(request, body);
			stopWatch.stop();
			traceResponse(clientHttpResponse, stopWatch);	
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
		return clientHttpResponse;
	}

	private void traceResponse(ClientHttpResponse clientHttpstringBuilderResponse, StopWatch stopWatch) {
		LOGGER.info("============================response begin==========================================");
		try {
			if (clientHttpstringBuilderResponse != null && clientHttpstringBuilderResponse.getBody() != null && isReadableResponse(clientHttpstringBuilderResponse)) {
				StringBuilder stringBuilder = new StringBuilder();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientHttpstringBuilderResponse.getBody(), "UTF-8"));
		        String line = bufferedReader.readLine();
		        while (line != null) {
		        	stringBuilder.append(line);
		        	stringBuilder.append('\n');
		            line = bufferedReader.readLine();
		        }
		        LOGGER.info("Status code  : {}", clientHttpstringBuilderResponse.getStatusCode());
		        LOGGER.info("Status text  : {}", clientHttpstringBuilderResponse.getStatusText());
		        LOGGER.info("Headers      : {}", clientHttpstringBuilderResponse.getHeaders());
		        LOGGER.info("Response body: {}", stringBuilder.toString());
			} else {
				LOGGER.info("clientHttpstringBuilderResponse is null");
			}
			
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		LOGGER.info("Response from called   ::: " + LocalTime.MIN.plusSeconds(stopWatch.getTotalTimeMillis()));
		LOGGER.info("=======================response end=================================================");
	}

	private void traceRequest(HttpRequest request, byte[] body) throws Exception {
		try {
			LOGGER.info("===========================request begin================================================");
			LOGGER.info("URI			: {}", request.getURI());
			LOGGER.info("Method			: {}", request.getMethod());
			LOGGER.info("Headers		: {}", request.getHeaders());
			LOGGER.info("Request body	: {}", getRequestBody(body));
			LOGGER.info("===========================request end==================================================");
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
	}
	
	private String getRequestBody(byte[] body) throws UnsupportedEncodingException {
        if (body != null && body.length > 0) {
            return getBodyAsJson(new String(body, "UTF-8"));
        } else {
            return null;
        }
    }
	
	private String getBodyAsJson(String bodyString) {
        if (bodyString == null || bodyString.length() == 0) {
            return null;
        } else {
            if (isValidJSON(bodyString)) {
                return bodyString;
            } else {
                bodyString.replaceAll("\"", "\\\"");
                return "\"" + bodyString + "\"";
            }
        }
    }
	
	public boolean isValidJSON(final String json) {
        boolean valid = false;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(json);
        } catch(IOException e) {
            valid = false;
        }
        return valid;
    }
	
	private boolean isReadableResponse(ClientHttpResponse response) {
        for (String contentType: response.getHeaders().get("Content-Type")) {
            if (isReadableContentType(contentType)) {
                return true;
            }
        }
        return false;
    }
	
	private boolean isReadableContentType(String contentType) {
        return contentType.startsWith("application/json")
                || contentType.startsWith("text");
    }

}
