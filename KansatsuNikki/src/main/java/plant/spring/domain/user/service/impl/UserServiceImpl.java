package plant.spring.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.Users;
import plant.spring.domain.user.service.UserService;
import plant.spring.repository.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;
	
	/** アカウント1件登録 */
	public int signup(Users user) {
		
		//パスワード暗号化
		String rawPassword = user.getPass();
		user.setPass(encoder.encode(rawPassword));
		
		return mapper.insertOne(user);
	}
	
	/** ログインユーザー情報取得 */
	public Users getLoginUser(String account) {
		return mapper.findLoginUser(account);
	}
}
