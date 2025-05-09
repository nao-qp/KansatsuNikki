package plant.spring.domain.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.PlantFiles;
import plant.spring.domain.user.service.PlantFileService;
import plant.spring.repository.PlantFileMapper;

@Service
public class PlantFileServiceImpl implements PlantFileService {
	
	@Autowired
	private PlantFileMapper mapper;

	/** select **/
	/** 植物画像データ一覧取得 **/
	@Override
	public List<PlantFiles> getPlantFiles(Integer id) {
		return mapper.findMany(id);
	}
	
	/** 画像ファイルパスを取得 **/
	@Override
	public String getFilePath(Integer id) {
		return mapper.getFilePath(id);
	}
	
	/** insert **/
	/** 植物画像データ1件登録 **/
	@Override
	public int addPlantFile(PlantFiles plantFile) {
		return mapper.insertOne(plantFile);
	}
	
	/** update **/
	/** 植物画像データファイル名1件更新 **/
	@Override
	public void editPlantFile(Integer id, String fileName) {
		mapper.updateOne(id, fileName);
	}
	
	/** 植物画像データ順番1件更新 **/
	@Override
	public void updateDisplayOrder(Integer plantFileId, 
			Integer plantId, Integer displayOrder) {
		mapper.updateDisplayOrder(plantFileId, plantId, displayOrder);
	}
	
	/** 植物画像データ削除(is_Deleted=1) **/
	@Override
	public void updIsDeleted(Integer id) {
		mapper.updIsDeleted(id);
	}
}
