package plant.spring.repository;

import org.apache.ibatis.annotations.Mapper;

import plant.spring.domain.user.model.Profiles;

@Mapper
public interface ProfileMapper {

	/** プロフィール初期データ1件作成 */
	public int insertOne(Integer id, String nickname);

	/** プロフィール情報1件取得 */
	public Profiles findOne(Integer id);


}
