package plant.spring.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddPlantForm {
	private String name;
	private String detail;
	private MultipartFile[] files;
}
