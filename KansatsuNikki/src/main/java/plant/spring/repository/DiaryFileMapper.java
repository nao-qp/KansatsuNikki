package plant.spring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import plant.spring.domain.user.model.DiaryFiles;

@Mapper
public interface DiaryFileMapper {

	/** select **/
	/** 日記画像データ取得(複数) **/
	public List<DiaryFiles> getDiaryFiles(Integer diariesId);
	
	/** 日記画像データ1件取得 **/
	public String getFilePath(Integer diariesId);
	
	/** insert **/
	/** 観察日記画像データ1件登録 */
	public int insertOne(DiaryFiles diaryFile);
	
	/** update **/
	/** 観察日記画像データファイル名1件更新 **/
	public void updDiaryFileName(@Param("id") Integer id, @Param("fileName") String fileName);
	
	/** 観察日記画像データ1件削除 **/
	public void updIsDeleted(Integer id);
	
	/** 植物画像データ順番1件更新 **/
	public void updateDisplayOrder(@Param("diaryFileId") Integer diaryFileId, 
			@Param("diaryId") Integer diaryId, @Param("displayOrder") Integer displayOrder);
	
}
