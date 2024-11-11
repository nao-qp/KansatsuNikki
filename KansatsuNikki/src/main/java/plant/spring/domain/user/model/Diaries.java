package plant.spring.domain.user.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Diaries {
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
}
