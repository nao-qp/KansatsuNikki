package plant.spring.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import plant.spring.validation.PasswordsMatch;

@Data
@PasswordsMatch
public class SignupForm {
	
	@NotBlank
	@Length(max = 30, message="{account.Length}")
	private String account;
	
	@NotBlank
	@Length(min = 6, max = 30)
	private String pass;
	
	@NotBlank
	@Length(min = 6, max = 30)
	private String confirmPass;
}

