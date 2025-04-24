package plant.spring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.DiaryFiles;

@Mapper
public interface DiaryFileMapper {

	/** 観察日記画像データ1件登録 */
	public int insertOne(DiaryFiles diaryFile);

	/** 観察日記画像データ1件更新 **/
	public void updateOne(@Param("id") Integer id, @Param("fileName") String fileName);
	
	/** 日記画像データ取得(複数) **/
	public List<DiaryFiles> getDiaryFiles(@Param("diariesId") Integer diariesId);

	
}
