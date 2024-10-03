package plant.spring.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.Profiles;
import plant.spring.domain.user.service.ProfileService;
import plant.spring.repository.ProfileMapper;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileMapper mapper;

	/** プロフィール初期データ1件作成 */
	@Override
	public int addProfile(Integer id, String nickname) {
		return mapper.insertOne(id, nickname);
	}

	/** プロフィール情報1件取得 */
	@Override
	public Profiles getProfile(Integer id) {
		return mapper.findOne(id);
	}

}
