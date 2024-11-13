package plant.spring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.PlantFiles;

@Mapper
public interface PlantFileMapper {

	/** 植物画像データ1件登録 */
	public int insertOne(PlantFiles plantFile);

	/** 植物画像データ1件更新 **/
	public void updateOne(@Param("id") Integer id, @Param("fileName") String fileName);
	
	/** 植物画像データ一覧取得 */
	public List<PlantFiles> findMany(Integer id);
}
