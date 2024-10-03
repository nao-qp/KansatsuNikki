package plant.spring.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import plant.spring.domain.user.model.Users;
import plant.spring.domain.user.service.UserService;

//ログイン後のページのURLをusersIdを使用して動的に作成する
//→→→→使用しない方向で。
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName(); // ユーザー名を取得（account）
        // ログインユーザー情報を取得
        Users user = userService.getLoginUser(username);
        int usersId = user.getId(); // usersテーブルのIDを取得

        // リダイレクト先を作成
        String redirectUrl = "/plant/mypage/" + usersId;
        response.sendRedirect(redirectUrl);
    }
}
