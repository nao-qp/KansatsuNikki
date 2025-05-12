package plant.spring.controller.plant;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import plant.spring.aop.annotation.Authenticated;
import plant.spring.domain.user.dto.PlantViewDto;
import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.model.Profiles;
import plant.spring.domain.user.service.DiaryService;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.ProfileService;
import plant.spring.domain.user.service.impl.CustomUserDetails;

@Controller
public class MyPageController {

	@Autowired
	private ProfileService profileService;
	@Autowired
	private PlantService plantService;
	@Autowired
	private DiaryService diaryService;

	//画像ディレクトリ取得
	@Value("${app.upload-dir-profile}")
	private String uploadDirProfilel;		//プロフィール画像
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像
	
	
	
	//植物一覧表示（マイページ）
	@Authenticated
	@GetMapping("/plant/mypage")
	public String getMyPage(Model model, Locale locale, @AuthenticationPrincipal CustomUserDetails user) {

        // 認証されたユーザーID
		Integer currentUserId = user.getId();
		model.addAttribute("userId", currentUserId);

		//プロフィール情報を取得
		Profiles profile = profileService.getProfile(currentUserId);
		model.addAttribute("profile", profile);
		//プロフィール画像保存先ディレクトリ設定
		model.addAttribute("uploadDirProfile", uploadDirProfilel);
		
		//ユーザーの植物数取得
		Integer plantCount = plantService.getCount(currentUserId);
		model.addAttribute("plantCount", plantCount);

		//ユーザーの観察日記数取得
		Integer diaryCount = diaryService.getCount(currentUserId);
		model.addAttribute("diaryCount", diaryCount);

		//ユーザーの植物一覧データを取得
		List<Plants> plantList = plantService.findMany(currentUserId);
		// 表示用PlantViewDtoのリストに変換(表示表urlを設定)
		List<PlantViewDto> plantViewDtoList = plantList.stream()
				.map(plant -> PlantViewDto.from(plant, uploadDirPlant))
				.toList();
		model.addAttribute("plantViewDtoList", plantViewDtoList);
		
		return "plant/mypage";
	}
	
	//植物削除処理
	@Authenticated
	@PostMapping("/plant/delete/{id}")
	public String deletePlant(Model model, @PathVariable("id") Integer id, Locale locale) {
		
        //植物削除（deleteフラグを1に更新）
        plantService.deletePlant(id);
        
		//削除実行後、植物一覧へリダイレクト
		return "redirect:/plant/mypage";
	}

}
