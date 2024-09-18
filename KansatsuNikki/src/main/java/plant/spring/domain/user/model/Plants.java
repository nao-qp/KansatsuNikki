package plant.spring.domain.user.model;

import lombok.Data;

@Data
public class Plants {
	private Integer id;
	private Integer usersId;
	private String name;
	private String detail;
	private boolean deleted;
	private String filePath;
}
