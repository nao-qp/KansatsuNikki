package plant.spring.controller.user;

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
		// ヘッダーのログアウトボタン表示判定に使用
		model.addAttribute("page", "login");
		 
		return "user/login";
	}

}
