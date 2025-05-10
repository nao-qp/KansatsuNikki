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
	private String pass;

	@NotBlank
	private String confirmPass;
}

