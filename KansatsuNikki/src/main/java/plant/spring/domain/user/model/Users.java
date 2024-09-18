package plant.spring.domain.user.model;

import lombok.Data;

@Data
public class Users {
	private Integer id;
	private String account;
	private String pass;
	private boolean isDeleted;
}
