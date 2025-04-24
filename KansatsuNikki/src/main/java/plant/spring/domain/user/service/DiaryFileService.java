package plant.spring.domain.user.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.DiaryFiles;

public interface DiaryFileService {

	/** 観察日記画像データ1件登録 **/
	public int addDiaryFile(DiaryFiles diaryFile);

	/** 観察日記画像データ1件更新 **/
	public void editDiaryFile(Integer id, String fileName);
	
	/** 日記画像データ取得(複数) **/
	public List<DiaryFiles> getDiaryFiles(@Param("diariesId") Integer diariesId);
}
