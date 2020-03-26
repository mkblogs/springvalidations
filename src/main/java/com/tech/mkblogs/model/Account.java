package com.tech.mkblogs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tech.mkblogs.validation.AlreadyExistsValidator.AlreadyExists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AlreadyExists(message = "{alreadyexists.account}")
public class Account{
	
	private Integer id;
	
	@NotEmpty(message = "{account.name.notempty}")
	@Size(min = 2,max = 50,message = "{account.name.size}")
	private String accountName;
	
	@NotEmpty(message = "{account.type.notempty}")
	@Size(min = 2,max = 50,message = "{account.type.size}")
	private String accountType;
	
	@NotNull(message = "{account.amount.notnull}")
	@DecimalMin(value = "100.00",inclusive = false,message = "{account.amount.min}")
    @Digits(integer=3, fraction=2,message = "{account.amount.decimal}" )
	private BigDecimal amount;
	
	private Integer createdBy;
	private String createdName;
	private LocalDateTime createdTs;
	private Integer lastModifiedBy;
	private String lastModifiedName;
	private LocalDateTime lastModifiedTs;
	private Integer version;
}
