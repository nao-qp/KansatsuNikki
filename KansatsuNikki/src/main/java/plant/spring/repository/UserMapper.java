package plant.spring.repository;

import org.apache.ibatis.annotations.Mapper;

import plant.spring.domain.user.model.Users;

@Mapper
public interface UserMapper {

	/** アカウント1件登録 */
	public int insertOne(Users user);
	
	/** ログインユーザー情報取得 */
	public Users findLoginUser(String account);
}
