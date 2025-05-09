package plant.spring.domain.user.service;

import java.time.LocalDate;
import java.util.List;

import plant.spring.domain.user.model.Diaries;

public interface DiaryService {

	/** select **/
	/**  ユーザーの観察日記数取得 */
	public Integer getCount(Integer id);
	
	/** 観察日記一覧取得 */
	public List<Diaries> getDiaries(Integer id);
	
	/** 観察日記IDからユーザーIDを取得 */
	public Integer getUserId(Integer id);
	
	/** 観察日記1件取得 */
	public Diaries getDiary(Integer id);
	
	/** insert **/
	/** 観察日記1件登録 */
	public int addDiary(Diaries diary);
	
	/** update **/
	/** 観察日記1件削除 */
	public int deleteDiary(Integer id);
	
	/** 観察日記1件更新 **/
	public int updDateDetail(Integer id, LocalDate observationDate, String detail);
}
