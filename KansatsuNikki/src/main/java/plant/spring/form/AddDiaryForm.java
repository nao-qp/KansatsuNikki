package plant.spring.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddDiaryForm {
	@NotNull(message = "観察日を入力して下さい。")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate observationDate;
	private String detail;
	private MultipartFile[] files;
}
