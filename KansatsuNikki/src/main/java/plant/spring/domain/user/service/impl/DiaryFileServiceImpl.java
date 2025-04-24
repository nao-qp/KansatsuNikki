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

	/** 観察日記画像データ1件登録 **/
	@Override
	public int addDiaryFile(DiaryFiles diaryFile) {
		return mapper.insertOne(diaryFile);
	}

	/** 観察日記画像データ1件更新 **/
	@Override
	public void editDiaryFile(Integer id, String fileName) {
		mapper.updateOne(id, fileName);
	}
	
	/** 日記画像データ取得(複数) **/
	@Override
	public List<DiaryFiles> getDiaryFiles(@Param("diariesId") Integer diariesId) {
		return mapper.getDiaryFiles(diariesId);
	}
}
