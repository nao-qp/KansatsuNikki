package plant.spring.controller.user;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import plant.spring.application.service.UserApplicationService;
import plant.spring.domain.user.model.Users;
import plant.spring.form.SignupForm;

@Controller
@Slf4j
public class SignupController {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserApplicationService userApplicationService;

	//新規登録ページ表示
	@GetMapping("/user/signup")
	public String getSignup(Model model, SignupForm form, Locale locale) {
		// ヘッダーにログアウトボタンを表示するか判定用に設定
		model.addAttribute("page", "signup");
		return "user/signup";
	}

	//新規登録処理
	@PostMapping("/user/signup")
	public String postSignup(Model model,
			@ModelAttribute @Validated SignupForm form, BindingResult bindingResult,
			Locale locale) {

		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NG:ユーザー登録画面に戻ります
			return getSignup(model, form, locale);
		}

		log.info(form.toString());

		//formをクラスに変換
		Users user = modelMapper.map(form, Users.class);

		//ユーザー登録
		try {
			//新規登録
			return registerUser(user, locale);

		} catch (DataIntegrityViolationException e) {
			//accountが重複した場合
			model.addAttribute("errorMessage", "このアカウントIDはすでに使用されています。別のアカウントIDをお試しください。");
			//再度新規登録ページを表示
			return getSignup(model, form, locale);
		}

	}
	//新規登録
	public String registerUser(Users user, Locale locale) {
		userApplicationService.registerNewUser(user, locale);

		return "redirect:/user/login";
	}

}
