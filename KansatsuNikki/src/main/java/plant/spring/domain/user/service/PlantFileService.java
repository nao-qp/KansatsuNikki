package plant.spring.domain.user.service;

import plant.spring.domain.user.model.PlantFiles;

public interface PlantFileService {

	/** 植物画像データ1件登録 **/
	public int addPlantFile(PlantFiles plantFile);

	/** 植物画像データ1件更新 **/
	public void editPlantFile(Integer id, String fileName);

}
