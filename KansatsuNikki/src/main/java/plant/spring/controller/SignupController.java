package plant.spring.controller;

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
import plant.spring.domain.user.service.UserService;
import plant.spring.form.SignupForm;

@Controller
@Slf4j
public class SignupController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private UserApplicationService userApplicationService;

	//新規登録ページ表示
	@GetMapping("/user/signup")
	public String getSignup(Model model, SignupForm form, Locale locale) {
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
			//新規登録、プロフィール初期データ登録、認証、マイページ表示
			return registerUser(user, locale);

			//※自動認証後、mypage表示が失敗するため後で確認。


			//////////認証情報確認//////////
			// 現在のユーザーの認証情報を取得
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//			if (authentication != null && authentication.isAuthenticated()) {
//			    // 認証されているユーザーの情報を取得
//			    String username = authentication.getName(); // ユーザー名
////			    Object principal = authentication.getPrincipal(); // ユーザーの詳細情報
//			    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 権限
//
//			    System.out.println(username);
//
//			    // 権限の表示
//			    authorities.forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
//			}
			//////////////////////////////


		} catch (DataIntegrityViolationException e) {
			//accountが重複した場合
			model.addAttribute("errorMessage", "このアカウントIDはすでに使用されています。別のアカウントIDをお試しください。");
			//再度新規登録ページを表示
			return getSignup(model, form, locale);
		}


	}

	//新規登録、プロフィール初期データ登録、認証、マイページ表示
	public String registerUser(Users user, Locale locale) {
		userApplicationService.registerNewUser(user, locale);
		//作成されたユーザーデータのIDを取得
		Integer usersId = userService.getLoginUser(user.getAccount()).getId();

		return "redirect:/plant/mypage/" + usersId;
	}

}
