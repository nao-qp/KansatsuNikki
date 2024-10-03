package plant.spring.domain.user.model;

import lombok.Data;

@Data
public class Users {
	private Integer id;
	private String account;
	private String pass;
	private boolean isDeleted;

	//SpringSecurityの認証情報作成のため一時的に権限を設定
	public String getRole() {
		return "ROLE_GENERAL";
	}
}
