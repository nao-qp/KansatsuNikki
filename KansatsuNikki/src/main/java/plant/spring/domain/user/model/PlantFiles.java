package plant.spring.domain.user.model;

import lombok.Data;

@Data
public class PlantFiles {
	private Integer id;
	private Integer plantsId;
	private Integer displayOrder;
	private String filePath;
	
	public PlantFiles(Integer plantsId, Integer displayOrder, String filePath) {
		this.plantsId = plantsId;
		this.displayOrder = displayOrder;
		this.filePath = filePath;
	}
}
