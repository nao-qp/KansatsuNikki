package plant.spring.domain.user.service;

import plant.spring.domain.user.model.Users;

public interface UserService {

	/** アカウント1件登録 */
	public int signup(Users user);

	/** ログインユーザー情報取得 */
	public Users getLoginUser(String account);
}
