package com.tech.mkblogs.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tech.mkblogs.filter.FilterDTO;
import com.tech.mkblogs.model.Account;

import lombok.extern.log4j.Log4j2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class AccountServiceTest {

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
	
	@Test
	public void saveAccount(@Autowired AccountService service) throws Exception {
		Account account = new Account();
		account.setAccountName("test");
		account.setAccountType("saving");
		account.setAmount(new BigDecimal(300));
		account.setCreatedBy(1);
		account.setCreatedName("Test");
		service.saveAccount(account);
		log.info(account.toString());
		
	}
	
	@Test
	public void saveAccountWithNamedJDBCTemplate(@Autowired AccountService service) throws Exception {
		Account account = new Account();
		account.setAccountName("namedjdbc");
		account.setAccountType("saving");
		account.setAmount(new BigDecimal(300));
		account.setCreatedBy(1);
		account.setCreatedName("Test");
		service.saveAccountWithNamedJDBCTemplate(account);
		log.info(account.toString());
	}
	
	@Test
	public void updateAccount(@Autowired AccountService service) throws Exception {
		Account account = new Account();
		account.setId(10);
		account.setAccountName("namedjdbcone");
		account.setAccountType("saving");
		account.setAmount(new BigDecimal(300));
		account.setLastModifiedBy(1);
		account.setLastModifiedName("modified_name");
		try {
			service.updateAccount(account);
			log.info(account.toString());
		}catch(Exception e){
			log.info(e.getMessage());
		}
		
	}
	
	@Test
	public void getAccountData(@Autowired AccountService service) throws Exception{
		Account account = new Account();
		account.setAccountName("test");
		account.setAccountType("saving");
		account.setAmount(new BigDecimal(300));
		account.setCreatedBy(1);
		account.setCreatedName("Test"); 
		account = service.saveAccount(account);
		account = service.getAccount(account.getId());
		log.info(account.toString());
	}
	
	@Test
	public void getAllAccountData(@Autowired AccountService service) throws Exception{
		List<Account> list = new ArrayList<Account>();
		list = service.getAllData();
		for(Account account:list)
			log.info(account.toString());
	}
	
	@Test
	public void findByAmountTest(@Autowired AccountService service) throws Exception {
		BigDecimal amount = new BigDecimal(100);
		List<Account> list = new ArrayList<Account>();
		list = service.findByAmount(amount);
		for(Account account:list)
			log.info(account.toString());
	}
	
	@Test
	public void findByNameAndAmount(@Autowired AccountService service) throws Exception {
		BigDecimal amount = new BigDecimal(100);
		String name="test";
		List<Account> list = new ArrayList<Account>();
		list = service.findByNameAndAmount(name, amount);
		for(Account account:list)
			log.info(account.toString());
	}
	
	@Test
	public void search(@Autowired AccountService service) throws Exception {
		String name="test";
		String type = "Saving";
		FilterDTO dto = new FilterDTO();
		dto.setAccountName(name);
		dto.setAccountType(type);
		dto.setAmount("300");
		List<Account> list = new ArrayList<Account>();
		list = service.searchWithJdbcTemplate(dto);
		for(Account account:list)
			log.info(account.toString());
	}
}
