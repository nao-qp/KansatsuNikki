package plant.spring.repository;

import org.apache.ibatis.annotations.Mapper;

import plant.spring.domain.user.model.Profiles;

@Mapper
public interface ProfileMapper {

	/** プロフィール情報1件取得 */
	public Profiles findOne(Integer id);
}
