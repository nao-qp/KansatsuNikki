package plant.spring.domain.user.service;

import java.util.List;

import plant.spring.domain.user.model.Diaries;

public interface DiaryService {

	/**  ユーザーの観察日記数取得 */
	public Integer getCount(Integer id);
	
	/** 観察日記一覧取得 */
	public List<Diaries> getDiaries(Integer id);
	
	/** 観察日記IDからユーザーIDを取得 */
	public Integer getUserId(Integer id);
	
	/** 観察日記1件取得 */
	public Diaries getDiary(Integer id);
	
	/** 観察日記1件登録 */
	public int addDiary(Diaries diary);
}
