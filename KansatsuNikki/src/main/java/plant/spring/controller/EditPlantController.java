package plant.spring.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import plant.spring.domain.user.model.PlantFiles;
import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.service.PlantFileService;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.impl.CustomUserDetails;
import plant.spring.form.AddPlantForm;

@Controller
public class EditPlantController {
	
	@Autowired
	private PlantService plantService;
	
	@Autowired
	private PlantFileService plantFileService;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像
	
	
	//植物編集画面を表示
	@GetMapping("/plant/edit/{id}")
	public String getEditPlant(Model model, AddPlantForm form, Locale locale, @PathVariable("id") Integer id) {
		
		////認証情報チェック
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }
        
        // 認証されたユーザーのIDを取得
        Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        
		//植物情報取得
		Plants plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		
		//該当の植物がログイン中のユーザーのデータかチェック
		if (plant.getUsersId() != currentUserId) {
			//ログイン中のユーザーの植物でない場合は、ログインページへリダイレクト
			 return "redirect:/user/login";
		}
		
		//画像データ取得
		List<PlantFiles> plantFiles = plantFileService.getPlantFiles(id);
		
		for(PlantFiles plantFile: plantFiles) {
			String filePath = uploadDirPlant + plantFile.getFilePath();
			model.addAttribute("img", filePath);
		}
		
		return "plant/edit";
	}
	
}
