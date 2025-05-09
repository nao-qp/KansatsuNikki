package plant.spring.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddDiaryForm {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate observationDate;
	private String detail;
	private MultipartFile[] files;
}
