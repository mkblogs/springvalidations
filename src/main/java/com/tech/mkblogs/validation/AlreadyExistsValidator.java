package com.tech.mkblogs.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalTime;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.mkblogs.model.Account;
import com.tech.mkblogs.repository.JdbcAccountRepository;
import com.tech.mkblogs.validation.AlreadyExistsValidator.AlreadyExists;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AlreadyExistsValidator implements ConstraintValidator<AlreadyExists, Account>{

	@Autowired
	private JdbcAccountRepository repository;
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Constraint(validatedBy = AlreadyExistsValidator.class)
	public @interface AlreadyExists {
		String message() default "Invalid value";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
	}
	
	@Override
	public boolean isValid(Account inputValue, ConstraintValidatorContext context) {
		log.info("| Request Time - Start - isValid() " + LocalTime.now());
		boolean flag = true;
		if(inputValue.getId() == null) {
			flag = isValidObjectForSave(inputValue, context);
		}else {
			flag = isValidObjectForUpdate(inputValue, context);
		}
		return flag;
	}
	
	protected boolean isValidObjectForSave(Account inputValue,ConstraintValidatorContext context) {
		boolean statusFlag = true; //Default Success
		List<Account> list = repository.findByName(inputValue.getAccountName());
		if(list.size() > 0)
			statusFlag = false; //Failure
		
		return statusFlag;
	}
	
	protected boolean isValidObjectForUpdate(Account inputValue,ConstraintValidatorContext context) {
		boolean statusFlag = true; //Default Success
		List<Account> list = repository.findByName(inputValue.getAccountName());
		int counter = 0;
		for(Account account:list) {
			if (account.getId() != account.getId()) {
				counter++;
			}
		}
		if (counter > 0)
			statusFlag = false;
		return statusFlag;
	}
}
