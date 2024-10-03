package plant.spring.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import plant.spring.form.LoginForm;

@Controller
public class LoginController {

	//ログインページ表示
	@GetMapping("/user/login")
	public String getLogin(Model model, LoginForm form, Locale locale) {
		return "user/login";
	}

}
