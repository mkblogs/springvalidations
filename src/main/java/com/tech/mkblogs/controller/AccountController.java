package com.tech.mkblogs.controller;

import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tech.mkblogs.filter.FilterDTO;
import com.tech.mkblogs.model.Account;
import com.tech.mkblogs.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api")
@Validated
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@PostMapping("/account")
	public ResponseEntity<Account> saveAccount(@RequestBody Account account) throws Exception {
		log.info("| Request Time - Start - saveAccount() " + LocalTime.now());
		account = accountService.saveAccount(account);		
		log.info("| Request Time - End - saveAccount() " + LocalTime.now());
		return ResponseEntity.ok().body(account);
	}
	
	@PutMapping("/account")
	public ResponseEntity<Account> updateAccount(@RequestBody Account account) throws Exception {
		log.info("| Request Time - Start - updateAccount() " + LocalTime.now());
		account = accountService.updateAccount(account);		
		log.info("| Request Time - End - updateAccount() " + LocalTime.now());
		return ResponseEntity.ok().body(account);
	}
	
	@GetMapping("/account/{accountId}")
	public ResponseEntity<Account> getAccount(@PathVariable("accountId") 
	@Min(value = 1,message = "{account.id.min}" ) Integer accountId) throws Exception{
		log.info("| Request Time - Start - getAccount("+accountId+") " + LocalTime.now());
		Account account = accountService.getAccount(accountId);		
		log.info("| Request Time - End - getAccount("+accountId+") " + LocalTime.now());
		return ResponseEntity.ok().body(account);
	}
	
	@GetMapping("/account")
	public ResponseEntity<List<Account>> getAllAccount() throws Exception{
		log.info("| Request Time - Start - getAllAccount() " + LocalTime.now());
		List<Account> listAccount = accountService.getAllData();		
		log.info("| Request Time - End - getAllAccount() " + LocalTime.now());
		return ResponseEntity.ok().body(listAccount);
	}
	
	@GetMapping("/account/search")
	public ResponseEntity<List<Account>> search(@RequestParam("amount") String amount,
			@RequestParam("accountName") String accountName,
			@RequestParam("accountType") String accountType) throws Exception{
		log.info("| Request Time - Start - search() " + LocalTime.now());
		FilterDTO dto = new FilterDTO();
		dto.setAccountName(accountName);
		dto.setAccountType(accountType);
		dto.setAmount(amount);
		List<Account> listAccount = accountService.searchWithJdbcTemplate(dto);
		log.info("| Request Time - End - search() " + LocalTime.now());
		return ResponseEntity.ok().body(listAccount);
	}
}
