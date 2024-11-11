package plant.spring.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.DiaryFiles;
import plant.spring.domain.user.service.DiaryFileService;
import plant.spring.repository.DiaryFileMpper;

@Service
public class DiaryFileServiceImpl implements DiaryFileService {
	
	@Autowired
	private DiaryFileMpper mapper;

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
}
