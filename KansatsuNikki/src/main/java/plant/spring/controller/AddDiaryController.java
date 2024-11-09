package plant.spring.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.impl.CustomUserDetails;
import plant.spring.form.AddDiaryForm;
import plant.spring.form.AddPlantForm;

@Controller
public class AddDiaryController {

	@Autowired
	private PlantService plantService;
	
	//観察日記登録画面表示
	@GetMapping("/diary/add/{id}")
	public String getAddDiary(Model model, AddDiaryForm form, @PathVariable("id") Integer id, Locale locale) {
		
		//認証情報チェック
		
		//植物情報取得
		Plants plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		
		return "diary/add";
	}
	
	
	
	//観察日記登録処理
	@PostMapping("/diary/add")
	public String postAddDiary(Model model,
	@AuthenticationPrincipal CustomUserDetails user,
	@RequestParam("files") MultipartFile[] files,
	@ModelAttribute @Validated AddPlantForm form, BindingResult bindingResult,
	Locale locale) {
		
		
		
		
		
		//植物詳細画面へ
		return "redirect:/";
		
	}
	
	
}
