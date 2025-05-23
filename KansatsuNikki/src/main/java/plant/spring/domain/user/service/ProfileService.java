package plant.spring.domain.user.service;

import plant.spring.domain.user.model.Profiles;

public interface ProfileService {

	/** プロフィール初期データ1件作成 */
	public int addProfile(Integer id, String nickname);

	/** プロフィール情報1件取得 */
	public Profiles getProfile(Integer id);

	/** update **/
	/** ニックネームプロフィール1件更新 **/
	public int updNicknameProfile(Integer id, String nickname, String profile);
	
	/** ファイルパスクリア1件更新 **/
	public int updFilePathClear(Integer id);
	
	/** ファイルパス1件更新 **/
	public int updFilePath(Integer id, String filePath);
}
