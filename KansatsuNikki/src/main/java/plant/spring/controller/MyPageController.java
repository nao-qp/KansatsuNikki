package plant.spring.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.model.Profiles;
import plant.spring.domain.user.service.DiaryService;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.ProfileService;

@Controller
public class MyPageController {

	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PlantService plantService;

	@Autowired
	private DiaryService diaryService;
	
	//植物一覧表示（マイページ）
	@GetMapping("/plant/mypage/{id}")
	public String getMyPage(Model model, @PathVariable("id") Integer id, Locale locale) {

		//プロフィール情報を取得
		Profiles profile = profileService.findOne(id);
		model.addAttribute("profile", profile);

		//ユーザーの植物数取得
		Integer plantCount = plantService.getCount(id);
		model.addAttribute("plantCount", plantCount);
		
		//ユーザーの観察日記数取得
		Integer diaryCount = diaryService.getCount(id);
		model.addAttribute("diaryCount", diaryCount);
		
		//ユーザーの植物一覧データを取得
		List<Plants> plantList = plantService.findMany(id);
		model.addAttribute("plantList", plantList);
		
		return "plant/mypage";
	}


}
