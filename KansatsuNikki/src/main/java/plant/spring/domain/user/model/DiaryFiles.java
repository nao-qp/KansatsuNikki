package plant.spring.domain.user.model;

import lombok.Data;

@Data
public class DiaryFiles {
	private Integer id;
	private Integer diariesId;
	private Integer displayOrder;
	private String filePath;

	public DiaryFiles(Integer diariesId, Integer displayOrder, String filePath) {
		this.diariesId = diariesId;
		this.displayOrder = displayOrder;
		this.filePath = filePath;
	}
	
}
