package plant.spring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import plant.spring.domain.user.model.Diaries;

@Mapper
public interface DiaryMapper {

	/**  ユーザーの観察日記数取得 */
	public Integer getCount(Integer id);

	/** 観察日記一覧取得 */
	public List<Diaries> findMany(Integer id);
	
	/** 観察日記IDからユーザーIDを取得 */
	public Integer findOneUserId(Integer id);
	
	/** 観察日記1件取得 */
	public Diaries findOne(Integer id);
	
	/** 観察日記1件登録 */
	public int insertOne(Diaries diary);
}
