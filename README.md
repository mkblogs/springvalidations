# Spring Validations
In this example we have used default validations as well as custom validations 

###Default Validations
 -@NotEmpty
 -@Size
 -@NotNull
 -@DecimalMin
 -@Digits
 -etc..
 
 ```java
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
 ```
 ###Custom Validation 
 I have created custom validation like `@AlreadyExists`, it will check account name is already exists or not.
 ```java
	 @AlreadyExists(message = "{alreadyexists.account}")
	 
	 @Documented
	 @Retention(RetentionPolicy.RUNTIME)
	 @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	 @Constraint(validatedBy = AlreadyExistsValidator.class)
	 public @interface AlreadyExists {
		String message() default "Invalid value";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
	 }
	 
	 public class AlreadyExistsValidator implements ConstraintValidator<AlreadyExists, Account>{
	 }
````	 


