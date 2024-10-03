package plant.spring.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryMapper {

	/**  ユーザーの観察日記数取得 */
	public Integer getCount(Integer id);

}
