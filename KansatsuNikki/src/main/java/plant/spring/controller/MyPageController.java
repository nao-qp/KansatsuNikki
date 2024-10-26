package plant.spring.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
	@GetMapping("/plant/mypage")
	public String getMyPage(Model model, Locale locale) {

		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }

        // 認証されたユーザーのIDを取得
        Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();

		//プロフィール情報を取得
		Profiles profile = profileService.getProfile(currentUserId);
		model.addAttribute("profile", profile);
		//プロフィール画像保存先ディレクトリ設定
		model.addAttribute("uploadDirProfilet", uploadDirProfilel);
		
		//ユーザーの植物数取得
		Integer plantCount = plantService.getCount(currentUserId);
		model.addAttribute("plantCount", plantCount);

		//ユーザーの観察日記数取得
		Integer diaryCount = diaryService.getCount(currentUserId);
		model.addAttribute("diaryCount", diaryCount);

		//ユーザーの植物一覧データを取得
		List<Plants> plantList = plantService.findMany(currentUserId);
		model.addAttribute("plantList", plantList);
		//植物画像保存先ディレクトリ設定
		model.addAttribute("uploadDirPlant", uploadDirPlant);
		
		return "plant/mypage";
	}


	//植物一覧表示（他ユーザー参照用）
	@GetMapping("/plant/other/{id}")
	public String getOtherPage(Model model, @PathVariable("id") Integer id, Locale locale) {

		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
        		&& !(authentication instanceof AnonymousAuthenticationToken)) {

        	// 認証されたユーザーのIDを取得
            Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();

            // IDを比較
            // ログイン中のアカウントのIDだった場合、mypageへリダイレクト
            if (currentUserId.equals(id)) {
                return "redirect:/plant/mypage";
            }
        }

		//プロフィール情報を取得
		Profiles profile = profileService.getProfile(id);
		model.addAttribute("profile", profile);
		//プロフィール画像保存先ディレクトリ設定
		model.addAttribute("uploadDirProfilet", uploadDirProfilel);
				
		//ユーザーの植物数取得
		Integer plantCount = plantService.getCount(id);
		model.addAttribute("plantCount", plantCount);

		//ユーザーの観察日記数取得
		Integer diaryCount = diaryService.getCount(id);
		model.addAttribute("diaryCount", diaryCount);

		//ユーザーの植物一覧データを取得
		List<Plants> plantList = plantService.findMany(id);
		model.addAttribute("plantList", plantList);
		//植物画像保存先ディレクトリ設定
		model.addAttribute("uploadDirPlant", uploadDirPlant);

		return "plant/mypage";
	}
	
	//植物削除処理
	@PostMapping("/plant/delete/{id}")
	public String deletePlant(Model model, @PathVariable("id") Integer id, Locale locale) {
		
		////削除処理前に、認証情報を確認////
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }
		
        //植物削除（deleteフラグを1に更新）
        plantService.deletePlant(id);
        
        
		//削除実行後、植物一覧へリダイレクト
		return "redirect:/plant/mypage";
	}
	

}
