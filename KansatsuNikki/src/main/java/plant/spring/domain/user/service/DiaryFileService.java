package plant.spring.domain.user.service;

import java.util.List;

import plant.spring.domain.user.model.DiaryFiles;

public interface DiaryFileService {

	/** select **/
	/** 日記画像データ取得(複数) **/
	public List<DiaryFiles> getDiaryFiles(Integer diariesId);
	
	/** 日記画像データ1件取得 **/
	public String getFilePath(Integer diariesId);
	
	/** insert **/
	/** 観察日記画像データ1件登録 **/
	public int addDiaryFile(DiaryFiles diaryFile);
	
	/** update **/
	/** 観察日記画像データファイル名1件更新 **/
	public void updDiaryFileName(Integer id, String fileName);
	
	/** 観察日記画像データ1件削除 **/
	public void updIsDeleted(Integer id);
	
	/** 植物画像データ順番1件更新 **/
	public void updateDisplayOrder(Integer diaryFileId, 
			Integer diaryId, Integer displayOrder);
}
