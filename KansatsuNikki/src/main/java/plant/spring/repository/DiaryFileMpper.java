package plant.spring.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.DiaryFiles;

@Mapper
public interface DiaryFileMpper {

	/** 観察日記画像データ1件登録 */
	public int insertOne(DiaryFiles diaryile);

	/** 観察日記画像データ1件更新 **/
	public void updateOne(@Param("id") Integer id, @Param("fileName") String fileName);
}
