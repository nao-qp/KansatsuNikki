package plant.spring.controller.plant;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import plant.spring.domain.user.dto.DiaryViewDto;
import plant.spring.domain.user.model.Diaries;
import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.model.Profiles;
import plant.spring.domain.user.service.DiaryService;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.ProfileService;

@Controller
public class PlantDetailController {

	@Autowired
	private ProfileService profileService;
	@Autowired
	private PlantService plantService;
	@Autowired
	private DiaryService diaryService;
	
	@Autowired
	private MessageSource messageSource;
	
	//画像ディレクトリ取得
	@Value("${app.upload-dir-profile}")
	private String uploadDirProfilel;		//プロフィール画像
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像
	@Value("${app.upload-dir-diary}")
	private String uploadDirDiary;		//観察日記画像
		
	//植物詳細表示
	@GetMapping("/plant/detail/{id}")
	public String getDetail(Model model, Locale locale, @PathVariable("id") Integer id) {
		
		////ユーザー情報取得
        //植物IDからユーザーIDを取得する
        Integer UserId = plantService.getUserId(id);
        model.addAttribute("UserId", UserId);
        
      //TODO:現在ログインしているユーザーかどうかで編集ボタンを表示するかどうか判定する
     // 現在のユーザーの認証情報を取得
//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
         
		//ニックネーム
        Profiles profile = profileService.getProfile(UserId);
        model.addAttribute("profile", profile);
        
        //プロフィール画像保存先ディレクトリ設定
      	model.addAttribute("uploadDirProfile", uploadDirProfilel);
      	
      	//ユーザーの植物数取得
 		String plantCount = String.format("%,d", plantService.getCount(UserId));
  		model.addAttribute("plantCount", plantCount);

  		//ユーザーの観察日記数取得
  		String diaryCount = String.format("%,d", diaryService.getCount(UserId));
  		model.addAttribute("diaryCount", diaryCount);
		
  		String plantTitle = messageSource.getMessage("plant", null, Locale.JAPAN);
  		String diaryTitle = messageSource.getMessage("diary", null, Locale.JAPAN);
		
  		StringBuilder sb = new StringBuilder();
  		sb.append(plantTitle);
  		sb.append(" ");
  		sb.append(plantCount);
  		sb.append("　");		//全角スペース
  		sb.append(diaryTitle);
  		sb.append(" ");
  		sb.append(diaryCount);
  		String plantDiaryCount = sb.toString();
  		model.addAttribute("plantDiaryCount", plantDiaryCount);
  		
		////植物情報
  		//植物画像保存先ディレクトリ設定
  		model.addAttribute("uploadDirPlant", uploadDirPlant);
  		
		//植物情報取得
		Plants plant = plantService.getPlant(id);
  		model.addAttribute("plant", plant);

		////観察日記情報
  		//観察日記画像保存先ディレクトリ設定
  		model.addAttribute("uploadDirDiary", uploadDirDiary);
  		
  		//観察日記一覧取得
		List<Diaries> diaries = diaryService.getDiaries(id);
  		// 日付表示フォーマット
  		List<DiaryViewDto> diaryViewDtoList = diaries.stream()
				.map(diary -> DiaryViewDto.from(diary))
				.toList();
  		model.addAttribute("diaryViewDtoList", diaryViewDtoList);
  		
		return "plant/plant-detail";
	}
	
	//観察日記削除処理
	@PostMapping("/diary/delete/{plantsId}/{id}")
	public String deleteDiary(Model model, Locale locale, @PathVariable("plantsId") Integer plantsId, @PathVariable("id") Integer id) {
		
		////削除処理前に、認証情報を確認////
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }
		
        //観察日記削除（deleteフラグを1に更新）
        diaryService.deleteDiary(id);
        
        
		//削除実行後、植物詳細画面へリダイレクト
		return "redirect:/plant/detail/{plantsId}";
	}

}
