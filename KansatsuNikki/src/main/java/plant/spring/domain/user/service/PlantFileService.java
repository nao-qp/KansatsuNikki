package plant.spring.domain.user.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.PlantFiles;

public interface PlantFileService {

	/** select **/
	/** 植物画像データ一覧取得 */
	public List<PlantFiles> getPlantFiles(Integer id);
	
	/** 画像ファイルパスを取得 **/
	public String getFilePath(Integer id);
	
	/** insert **/
	/** 植物画像データ1件登録 **/
	public int addPlantFile(PlantFiles plantFile);
	
	/** update **/
	/** 植物画像データファイル名1件更新 **/
	public void editPlantFile(Integer id, String fileName);
	
	/** 植物画像データ順番1件更新 **/
	public void updateDisplayOrder(@Param("plantFileId") Integer plantFileId, 
			@Param("plantId") Integer plantId, @Param("displayOrder") Integer displayOrder);
	
	/** 植物画像データ削除(is_Deleted=1) **/
	public void updIsDeleted(@Param("id") Integer id);
}
