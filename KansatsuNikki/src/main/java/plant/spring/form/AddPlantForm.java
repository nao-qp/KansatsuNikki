package plant.spring.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddPlantForm {
	@NotBlank(message = "植物の名前を入力して下さい。")
	private String name;
	private String detail;
	private MultipartFile[] files;
}
