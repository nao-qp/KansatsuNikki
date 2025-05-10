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
	
	/** update **/
	/** ニックネームプロフィール1件更新 **/
	@Override
	public int updNicknameProfile(Integer id, String nickname, String profile) {
		return mapper.updNicknameProfile(id, nickname, profile);
	}
	
	/** ファイルパスクリア1件更新 **/
	@Override
	public int updFilePathClear(Integer id) {
		return mapper.updFilePathClear(id);
	}
	
	/** ファイルパス1件更新 **/
	public int updFilePath(Integer id, String filePath) {
		return mapper.updFilePath(id, filePath);
	}
	

}
