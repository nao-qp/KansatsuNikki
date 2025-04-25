package plant.spring.domain.user.dto;

import org.modelmapper.ModelMapper;

import lombok.Data;
import plant.spring.domain.user.model.Plants;

@Data
public class PlantViewDto {
	private Integer id;
	private String name;
	private String detail;
	private String imageUrl;	// 画面表示用に作成したパス

	// Plantsオブジェクトから変換メソッド
	public static PlantViewDto from(Plants plant, String uploadDirPlant) {
        ModelMapper modelMapper = new ModelMapper();
        PlantViewDto plantViewDto = modelMapper.map(plant, PlantViewDto.class);

        // 表示用URLを組み立て（追加フィールド）
        String imageUrl = (plant.getFilePath() != null && !plant.getFilePath().isEmpty())
            ? uploadDirPlant + plant.getFilePath() + "?v=" + System.currentTimeMillis()
            : "/images/plantImage.png";
        plantViewDto.setImageUrl(imageUrl);

        return plantViewDto;
    } 
}
