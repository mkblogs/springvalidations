package com.tech.mkblogs.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.tech.mkblogs.filter.FilterDTO;
import com.tech.mkblogs.model.Account;

public interface AccountRepository {

	public Integer count();
	public Integer saveAccount(Account account);
	public Integer saveAccountWithNamedJDBCTemplate(Account account);
    public Integer updateAccount(Account account);
    public Integer deleteById(Integer id);
    public List<Account> findAll();
    public List<Account> findByAmount(BigDecimal price);
    public List<Account> findByName(String accountName);
    public List<Account> findByNameAndAmount(String name,BigDecimal price);
    public Optional<Account> findById(Integer id);
    public String getNameById(Long id);
    public List<Account> searchWithJdbcTemplate(FilterDTO dto);
}
