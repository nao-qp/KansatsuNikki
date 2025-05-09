package plant.spring.domain.user.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;

import lombok.Data;
import plant.spring.application.util.FormatUtils;
import plant.spring.domain.user.model.Diaries;
@Data
public class DiaryViewDto {
	private Integer id;
	private Integer plantsId;
	private Integer usersId;
	private LocalDate observationDate;
	private String detail;
	private boolean deleted;
	private String filePath;
	private LocalDate createDateTime;
	private LocalDate updateDateTime;
	private String name;			/* 植物名 */
	private String observationDateStr;	/* 表示用観察日（追加フィールド） */

	// Diariesオブジェクトから変換メソッド
	public static DiaryViewDto from(Diaries diary) {
        ModelMapper modelMapper = new ModelMapper();
        DiaryViewDto diaryViewDto = modelMapper.map(diary, DiaryViewDto.class);

        // 表示用の日付フォーマットを適用してセット（追加フィールド）
        if (diary != null && diary.getObservationDate() != null) {
        	// yyyy年MM月dd日(曜日)
      		diaryViewDto.setObservationDateStr(FormatUtils.formatToJapaneseDateString(diary.getObservationDate()));
        } 

        return diaryViewDto;
    } 
}
