package plant.spring.domain.user.service;

import plant.spring.domain.user.model.DiaryFiles;

public interface DiaryFileService {

	/** 観察日記画像データ1件登録 **/
	public int addDiaryFile(DiaryFiles diaryFile);

	/** 観察日記画像データ1件更新 **/
	public void editDiaryFile(Integer id, String fileName);
}
