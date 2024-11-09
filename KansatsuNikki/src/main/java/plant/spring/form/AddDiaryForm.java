package plant.spring.form;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddDiaryForm {
	private LocalDate observationDate;
	private String detail;
	private MultipartFile[] files;
}
