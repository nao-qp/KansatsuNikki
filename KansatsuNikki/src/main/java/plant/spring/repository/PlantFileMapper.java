package plant.spring.repository;

import org.apache.ibatis.annotations.Mapper;

import plant.spring.domain.user.model.PlantFiles;

@Mapper
public interface PlantFileMapper {

	/** 植物画像データ1件登録 */
	public int insertOne(PlantFiles plantFile);
}
