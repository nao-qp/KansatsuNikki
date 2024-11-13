package plant.spring.domain.user.service;

import java.util.List;

import plant.spring.domain.user.model.PlantFiles;

public interface PlantFileService {

	/** 植物画像データ1件登録 **/
	public int addPlantFile(PlantFiles plantFile);

	/** 植物画像データ1件更新 **/
	public void editPlantFile(Integer id, String fileName);

	/** 植物画像データ一覧取得 */
	public List<PlantFiles> getPlantFiles(Integer id);
}
