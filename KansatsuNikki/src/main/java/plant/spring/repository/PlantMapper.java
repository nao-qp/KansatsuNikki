package plant.spring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import plant.spring.domain.user.model.Plants;

@Mapper
public interface PlantMapper {

	/**  ユーザーの植物数取得 */
	public Integer getCount(Integer id);
	
	/** ユーザーの植物一覧データを取得 */
	public List<Plants> findMany(Integer id);
	
	/** 植物1件登録 */
	public int insertOne(Plants plant);
}
