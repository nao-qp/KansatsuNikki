package plant.spring.application.service;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
    @Autowired
    private AuthenticationManager authenticationManager;

	public Users registerNewUser(Users user, Locale locale) {
		//自動ログイン用パス退避
		String loginPass = user.getPass();
		
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
		
        // 自動ログイン処理
        authenticateUser(user.getAccount(), loginPass);


        //自動認証後mypage表示で失敗するので後で確認
      ////////////認証情報確認//////////
		// 現在のユーザーの認証情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
		    // 認証されているユーザーの情報を取得
		    String username = authentication.getName(); // ユーザー名
//		    Object principal = authentication.getPrincipal(); // ユーザーの詳細情報
		    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 権限

		    System.out.println(username);
		    
		    // 権限の表示
		    authorities.forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
		}
		//////////////////////////////
        
        
        return user;
    }

    private void authenticateUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
