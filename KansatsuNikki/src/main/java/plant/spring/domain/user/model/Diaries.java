package plant.spring.domain.user.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Diaries {
	private Integer id;
	private Integer plantsId;
	private LocalDate observationDate;
	private String detail;
	private boolean deleted;
}
