package plant.spring.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.Profiles;

@Mapper
public interface ProfileMapper {

	/** プロフィール初期データ1件作成 */
	public int insertOne(Integer id, String nickname);

	/** プロフィール情報1件取得 */
	public Profiles findOne(Integer id);

	/** update **/
	/** ニックネームプロフィール1件更新 **/
	public int updNicknameProfile(@Param("id") Integer id, 
			@Param("nickname") String nickname, @Param("profile") String profile);
	
	/** ファイルパスクリア1件更新 **/
	public int updFilePathClear(Integer id);
	
	/** ファイルパス1件更新 **/
	public int updFilePath(@Param("id") Integer id, @Param("filePath") String filePath);
	
}
