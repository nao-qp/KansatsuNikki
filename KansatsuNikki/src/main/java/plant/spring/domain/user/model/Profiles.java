package plant.spring.domain.user.model;

import lombok.Data;

@Data
public class Profiles {
	private Integer id;
	private Integer usersId;
	private String nickname;
	private String profile;
	private String filePath;
}
