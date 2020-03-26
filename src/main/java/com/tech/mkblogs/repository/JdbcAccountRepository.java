package com.tech.mkblogs.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tech.mkblogs.constants.SQLConstants;
import com.tech.mkblogs.filter.FilterDTO;
import com.tech.mkblogs.model.Account;

import lombok.extern.log4j.Log4j2;

@Repository
@Log4j2
@SuppressWarnings("unchecked")
public class JdbcAccountRepository implements AccountRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	 @Autowired
     private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	

	@Override
	public Integer saveAccount(Account account) {
		KeyHolder generatedKeyHolder =  new  GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement preparedStatement = connection.prepareStatement(SQLConstants.SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, account.getAccountName());
				preparedStatement.setString(2, account.getAccountType());
	            preparedStatement.setBigDecimal(3, account.getAmount());
	            
	            preparedStatement.setInt(4, account.getCreatedBy());
	            preparedStatement.setString(5, account.getCreatedName());            
	            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
	            
	            preparedStatement.setInt(7, 0);
				
				return preparedStatement;
			}
		}, generatedKeyHolder);
		return generatedKeyHolder.getKey() != null ? generatedKeyHolder.getKey().intValue() : 0;
	}
	
	public Integer saveAccountWithNamedJDBCTemplate(Account account) {
		KeyHolder generatedKeyHolder =  new  GeneratedKeyHolder();
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("accountName", account.getAccountName())
				.addValue("accountType", account.getAccountType())
				.addValue("amount", account.getAmount())
				.addValue("createdBy", account.getCreatedBy())
				.addValue("createdName", account.getCreatedName())
				.addValue("createdTs", Timestamp.from(Instant.now()))
				.addValue("version", 0);
		
		namedParameterJdbcTemplate.update(SQLConstants.SQL_INSERT_NAMED_JDBC, parameters,generatedKeyHolder);
		return generatedKeyHolder.getKey() != null ? generatedKeyHolder.getKey().intValue() : 0;
	}

	@Override
	public Integer updateAccount(Account account) {
		Object[] values = {account.getAccountName(),
				account.getAccountType(),account.getAmount(),account.getVersion(),
				account.getLastModifiedBy(),
				account.getLastModifiedName(),
				Timestamp.valueOf(LocalDateTime.now()),
				account.getId()};
		return jdbcTemplate.update(SQLConstants.SQL_UPDATE,values);
	}

	@Override
	public Integer count() {
		return jdbcTemplate.queryForObject(SQLConstants.SQL_COUNT_DATA, Integer.class);
	}
	
	@Override
	public Integer deleteById(Integer id) {
		return jdbcTemplate.update(SQLConstants.SQL_DELETE,id);
	}

	@Override
	public List<Account> findAll() {
		return jdbcTemplate.query(SQLConstants.SQL_GET_ALL_DATA,new AccountRowMapper());
	}
	
	@Override
	public Optional<Account> findById(Integer id) {
		try {
			Account account =  jdbcTemplate.queryForObject(SQLConstants.SQL_GET_ACCOUNT,
	                new Object[]{id},new AccountRowMapper());
			Optional<Account> optionalObject = Optional.of(account);
			return optionalObject;
		}catch(EmptyResultDataAccessException e) {
			return Optional.empty();
		}catch(Exception e1){
			return Optional.empty();
		}
	}

	
	@Override
	public List<Account> findByAmount(BigDecimal amount) {
		try {
			return jdbcTemplate.query(SQLConstants.SQL_GET_AMOUNT,new Object[]{amount},new AccountRowMapper());
			
		}catch(EmptyResultDataAccessException e) {
			return Collections.EMPTY_LIST;
		}catch(Exception e1){
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public List<Account> findByNameAndAmount(String name, BigDecimal amount) {
		return jdbcTemplate.query(SQLConstants.SQL_GET_NAME_AMOUNT,new Object[]{"%" + name + "%",amount},new AccountRowMapper());
	}

	@Override
	public String getNameById(Long id) {
		return jdbcTemplate.queryForObject(SQLConstants.SQL_GET_ACCOUNT_NAME,new Object[]{id},String.class);
	}
	
	public List<Account> searchWithJdbcTemplate(FilterDTO dto){
		
		String name = dto.getAccountName();
		String type = dto.getAccountType();
		String amount = dto.getAmount();
		
		String sql = SQLConstants.SQL_SEARCH_ACCOUNT_ONE;
		if(name != null && !(name.isEmpty()))
			sql = sql + " and account_name like  '%"+name+"%'";
		if(type != null && !(type.isEmpty()))
			sql = sql + " and account_type =  '"+type+"' ";
		if(amount != null && !(amount.isEmpty()))
			sql = sql + " and amount =  "+amount+" ";
		
		log.info(sql);
        return jdbcTemplate.query(sql,new AccountRowMapper());
	}
	
	@Override
	public List<Account> findByName(String accountName) {
		try {
			return jdbcTemplate.query(SQLConstants.SQL_GET_NAME,new Object[]{accountName},new AccountRowMapper());
			
		}catch(EmptyResultDataAccessException e) {
			return Collections.EMPTY_LIST;
		}catch(Exception e1){
			return Collections.EMPTY_LIST;
		}
	}
}
