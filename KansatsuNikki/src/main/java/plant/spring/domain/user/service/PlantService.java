package plant.spring.domain.user.service;

import java.util.List;

import plant.spring.domain.user.model.Plants;

public interface PlantService {

	/**  ユーザーの植物数取得 */
	public Integer getCount(Integer id);

	/**  ユーザーの植物一覧データを取得 */
	public List<Plants> findMany(Integer id);

	/** 植物1件登録 */
	public int addPlant(Plants plant);

	//** 植物1件削除 */
	public int deletePlant(Integer id);
	
	//** 植物IDからユーザーIDを取得 */
	public int getUserId(Integer id);
	
	//** 植物1件取得 */
	public Plants getPlant(Integer id);
}
