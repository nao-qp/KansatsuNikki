package plant.spring.domain.user.service;

import plant.spring.domain.user.model.Profiles;

public interface ProfileService {
	
	/** プロフィール情報1件取得 */
	public Profiles findOne(Integer id);


}
