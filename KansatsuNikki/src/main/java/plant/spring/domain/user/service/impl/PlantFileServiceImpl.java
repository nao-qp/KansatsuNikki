package plant.spring.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.PlantFiles;
import plant.spring.domain.user.service.PlantFileService;
import plant.spring.repository.PlantFileMapper;

@Service
public class PlantFileServiceImpl implements PlantFileService {

	@Autowired
	private PlantFileMapper mapper;

	/** 植物画像データ1件登録 **/
	@Override
	public int addPlantFile(PlantFiles plantFile) {
		return mapper.insertOne(plantFile);
	}

	/** 植物画像データ1件更新 **/
	@Override
	public void editPlantFile(Integer id, String fileName) {
		mapper.updateOne(id, fileName);
	}
}
