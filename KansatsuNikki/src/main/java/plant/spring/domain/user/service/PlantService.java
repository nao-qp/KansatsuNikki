package plant.spring.domain.user.service;

import java.util.List;

import plant.spring.domain.user.model.Plants;

public interface PlantService {
	
	/**  ユーザーの植物数取得 */
	public Integer getCount(Integer id);
	
	/**  ユーザーの植物一覧データを取得 */
	public List<Plants> findMany(Integer id);
	
}
