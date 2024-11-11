package plant.spring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import plant.spring.domain.user.model.Diaries;
import plant.spring.domain.user.model.Plants;

@Mapper
public interface PlantMapper {

	/**  ユーザーの植物数取得 */
	public Integer getCount(Integer id);

	/** ユーザーの植物一覧データを取得 */
	public List<Plants> findMany(Integer id);

	/** 植物1件登録 */
	public int insertOne(Plants plant);
	
	//** 植物1件削除 */
	public int updateDelOne(Integer id);
	
	//** 植物IDからユーザーIDを取得 */
	public int findOneUserId(Integer id);
	
	//** 植物1件取得 */
	public Plants findOne(Integer id);
	
	/** 観察日記1件登録 */
	public int addDiary(Diaries diary);
}
