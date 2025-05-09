package plant.spring.domain.user.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.DiaryFiles;
import plant.spring.domain.user.service.DiaryFileService;
import plant.spring.repository.DiaryFileMapper;

@Service
public class DiaryFileServiceImpl implements DiaryFileService {
	
	@Autowired
	private DiaryFileMapper mapper;

	/** select **/
	/** 日記画像データ取得(複数) **/
	@Override
	public List<DiaryFiles> getDiaryFiles(@Param("diariesId") Integer diariesId) {
		return mapper.getDiaryFiles(diariesId);
	}
	
	/** 日記画像データ1件取得 **/
	@Override
	public String getFilePath(Integer diariesId) {
		return mapper.getFilePath(diariesId);
	}
	
	/** insert **/
	/** 観察日記画像データ1件登録 **/
	@Override
	public int addDiaryFile(DiaryFiles diaryFile) {
		return mapper.insertOne(diaryFile);
	}

	/** update **/
	/** 観察日記画像データファイル名1件更新 **/
	@Override
	public void updDiaryFileName(Integer id, String fileName) {
		mapper.updDiaryFileName(id, fileName);
	}
	
	/** 観察日記画像データ1件削除 **/
	@Override
	public void updIsDeleted(Integer id) {
		mapper.updIsDeleted(id);
	}
	
	/** 植物画像データ順番1件更新 **/
	@Override
	public void updateDisplayOrder(Integer diaryFileId, 
			Integer diaryId, Integer displayOrder) {
		mapper.updateDisplayOrder(diaryFileId, diaryId, displayOrder);
	}
	
}
