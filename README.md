# Spring Validations
In this example I have used default validations as well as custom validations 

### Default Validations
 - @NotEmpty
 - @Size
 - @NotNull
 - @DecimalMin
 - @Digits
 - etc..
 
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
 ### Custom Validation 
 I have created custom validation like `@AlreadyExists`, it will check account name is already exists or not in database.
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
	 
	    @Autowired
	    private JdbcAccountRepository repository;
	 
	    @Override
		public boolean isValid(Account inputValue, ConstraintValidatorContext context) {
			log.debug("| Request Time - Start - isValid() " + LocalTime.now());
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
````	 


