package com.tech.mkblogs.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.mkblogs.filter.FilterDTO;
import com.tech.mkblogs.model.Account;
import com.tech.mkblogs.repository.JdbcAccountRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountService {
	
	@Autowired
	private JdbcAccountRepository repository;

	public Account saveAccount(Account account) throws Exception {
		log.info("Starting the saveAccount() method ");
		try {
			Integer generatedKey = repository.saveAccount(account);
			log.info("Generated Primary Key : " + generatedKey);
			account.setId(generatedKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Ended the saveAccount() method ");
		return account;
	}
	
	public Account saveAccountWithNamedJDBCTemplate(Account account) throws Exception {
		log.info("Starting the saveAccountWithNamedJDBCTemplate() method ");
		try {
			Integer generatedKey = repository.saveAccountWithNamedJDBCTemplate(account);
			log.info("Generated Primary Key : " + generatedKey);
			account.setId(generatedKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Ended the saveAccountWithNamedJDBCTemplate() method ");
		return account;
	}
	

	public Account updateAccount(Account account) throws Exception {
		log.info("Starting the updateAccount() method ");
		try {
			Optional<Account> dbObjectExists = repository.findById(account.getId());
			if(dbObjectExists.isPresent()) {
				Integer version = dbObjectExists.get().getVersion();
				version = version + 1;
				account.setVersion(version);
				repository.updateAccount(account);
			}else {
				throw new RuntimeException("Entity Not Found " + account.getId());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.info("Ended the updateAccount() method ");
		return account;
	}

	public Account getAccount(Integer id) throws Exception {
		Account account = null;
		log.info("Starting the getAccount() method ");
		try {
			Optional<Account> dbObjectExists = repository.findById(id);
			if(dbObjectExists.isPresent()) {
				account = dbObjectExists.get();
			}else {
				throw new RuntimeException("Entity Not Found " + id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Ended the getAccount() method ");
		return account;
	}

	public List<Account> getAllData() throws Exception {
		List<Account> list = new ArrayList<Account>();
		log.info("Starting the getAllData() method ");
		try {
			list = repository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Ended the getAllData() method ");
		return list;
	}
	
	public List<Account> searchWithJdbcTemplate(FilterDTO dto) throws Exception {
		List<Account> list = new ArrayList<Account>();
		log.info("Starting the search() method ");
		try {
			list = repository.searchWithJdbcTemplate(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Ended the search() method ");
		return list;
	}
	
	public List<Account> findByAmount(BigDecimal amount) throws Exception {
		List<Account> list = new ArrayList<Account>();
		log.info("Starting the findByAmount() method ");
		try {
			list = repository.findByAmount(amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Ended the findByAmount() method ");
		return list;
	}
	
	public List<Account> findByNameAndAmount(String name,BigDecimal amount) throws Exception {
		List<Account> list = new ArrayList<Account>();
		log.info("Starting the findByNameAndAmount() method ");
		try {
			list = repository.findByNameAndAmount(name, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Ended the findByNameAndAmount() method ");
		return list;
	}
	
	
}
