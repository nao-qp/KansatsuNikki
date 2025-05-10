package plant.spring.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EditProfileForm {
	private String nickname;
	private String profile;
	private MultipartFile[] files;
}
