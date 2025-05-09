package plant.spring.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.Diaries;

@Mapper
public interface DiaryMapper {

	/** select **/
	/**  ユーザーの観察日記数取得 */
	public Integer getCount(Integer id);

	/** 観察日記一覧取得 */
	public List<Diaries> findMany(Integer id);
	
	/** 観察日記IDからユーザーIDを取得 */
	public Integer findOneUserId(Integer id);
	
	/** 観察日記1件取得 */
	public Diaries findOne(Integer id);
	
	/** insert **/
	/** 観察日記1件登録 */
	public int insertOne(Diaries diary);
	
	/** update **/
	/** 観察日記1件削除 */
	public int updateDelOne(Integer id);
	
	/** 観察日記1件更新 **/
	public int updDateDetail(@Param("id") Integer id, 
			@Param("observationDate") LocalDate observationDate, @Param("detail") String detail);
	
}
