package plant.spring.application.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.Users;
import plant.spring.domain.user.service.ProfileService;
import plant.spring.domain.user.service.UserService;

@Service
public class UserApplicationService {

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private MessageSource messageSource;

	public Users registerNewUser(Users user, Locale locale) {

        // ユーザーをデータベースに保存
		int result = userService.signup(user);

		//ユーザー登録成功時に、プロフィール初期データを作成
		if (result == 1) {
			//作成されたユーザーデータのIDを取得
			Integer usersId =  userService.getLoginUser(user.getAccount()).getId();
			//プロフィール初期データ作成
			String nickname = messageSource.getMessage("nickname", null, locale);
			profileService.addProfile(usersId, nickname);
		}

        return user;
    }
}
