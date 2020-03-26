package com.tech.mkblogs.controller;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tech.mkblogs.model.Account;
import com.tech.mkblogs.response.AccountErrorResponse;
import com.tech.mkblogs.response.ErrorObject;

import lombok.extern.log4j.Log4j2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class AccountControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@BeforeAll
	public static void beforeEachTest() throws Exception {
		log.info("==================================================================================");
		log.info("This is executed before each Test");
	}

	@AfterAll
	public static void afterEachTest() {		
		log.info("This is exceuted after each Test");
		log.info("==================================================================================");
	}
	
	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return objectMapper.readValue(json, clazz);
	}
	
	@Test
	public void testAccountSaveObject() throws Exception {
		String url = "/api/account";
		String jsonString = "{" 
								+ " \"accountName\": \"test\"," 
								+ " \"accountType\": \"Saving\","
								+ " \"amount\":100.90," 
								+ " \"createdBy\":1," 
								+ " \"createdName\": \"test\" " 
							+ " } ";
		log.info(jsonString);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON)
				.content(jsonString).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(requestBuilder).andReturn();
		System.out.println(result.getResponse().getContentAsString());
		try {
			String responseStr = result.getResponse().getContentAsString();
			if(responseStr.contains("error")) {
				AccountErrorResponse errorResponse = mapFromJson(result.getResponse().getContentAsString(),AccountErrorResponse.class);
				log.info("==================================================================================");
				for(ErrorObject error:errorResponse.getErrorList()) {
					log.info(error);
				}
				log.info("==================================================================================");
			}else {
				log.info("==================================================================================");
				Account account = mapFromJson(result.getResponse().getContentAsString(),Account.class);
				log.info(account);
				log.info("==================================================================================");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAccountUpdateObject() throws Exception {
		String url = "/api/account";
		String jsonString = "{" 
								+ " \"id\":1," 
								+ " \"accountName\": \"test\"," 
								+ " \"accountType\": \"Saving\","
								+ " \"amount\":100.90," 
								+ " \"createdBy\":1," 
								+ " \"createdName\": \"test\" " 
							+ " } ";
		log.info(jsonString);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url).accept(MediaType.APPLICATION_JSON)
				.content(jsonString).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(requestBuilder).andReturn();
		try {
			String responseStr = result.getResponse().getContentAsString();
			if(responseStr.contains("error")) {
				AccountErrorResponse errorResponse = mapFromJson(result.getResponse().getContentAsString(),AccountErrorResponse.class);
				log.info("==================================================================================");
				for(ErrorObject error:errorResponse.getErrorList()) {
					log.info(error);
				}
				log.info("==================================================================================");
			}else {
				log.info("==================================================================================");
				Account account = mapFromJson(result.getResponse().getContentAsString(),Account.class);
				log.info(account);
				log.info("==================================================================================");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAccountGetObject() throws Exception {
		String url = "/api/account/1";
		

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(requestBuilder).andReturn();
		try {
			String responseStr = result.getResponse().getContentAsString();
			if(responseStr.contains("error")) {
				AccountErrorResponse errorResponse = mapFromJson(result.getResponse().getContentAsString(),AccountErrorResponse.class);
				log.info("==========================Error Description =====================================");
				for(ErrorObject error:errorResponse.getErrorList()) {
					log.info(error);
				}
				log.info("==========================Error Description =====================================");
			}else {
				log.info("==================================================================================");
				Account account = mapFromJson(result.getResponse().getContentAsString(),Account.class);
				log.info(account);
				log.info("==================================================================================");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
