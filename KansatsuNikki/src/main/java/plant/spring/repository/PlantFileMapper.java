package plant.spring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.PlantFiles;

@Mapper
public interface PlantFileMapper {

	/** select **/
	/** 植物画像データ一覧取得 */
	public List<PlantFiles> findMany(Integer id);
	
	/** 画像ファイルパスを取得 **/
	public String getFilePath(Integer id);
	
	/** insert **/
	/** 植物画像データ1件登録 */
	public int insertOne(PlantFiles plantFile);

	/** update **/
	/** 植物画像データファイル名1件更新 **/
	public void updateOne(@Param("id") Integer id, @Param("fileName") String fileName);
	
	/** 植物画像データ順番1件更新 **/
	public void updateDisplayOrder(@Param("plantFileId") Integer plantFileId, 
			@Param("plantId") Integer plantId, @Param("displayOrder") Integer displayOrder);
	
	/** 植物画像データ削除(is_Deleted=1) **/
	public void updIsDeleted(@Param("id") Integer id);

}
