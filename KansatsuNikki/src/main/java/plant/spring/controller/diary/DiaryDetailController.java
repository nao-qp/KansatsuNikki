package plant.spring.controller.diary;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import plant.spring.aop.annotation.Authenticated;
import plant.spring.application.util.FormatUtils;
import plant.spring.domain.user.model.Diaries;
import plant.spring.domain.user.model.Profiles;
import plant.spring.domain.user.service.DiaryService;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.ProfileService;
import plant.spring.domain.user.service.impl.CustomUserDetails;

@Controller
public class DiaryDetailController {

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
	@Value("${app.upload-dir-diary}")
	private String uploadDirDiary;		//観察日記画像
	
	//観察日記詳細表示
	@Authenticated
	@GetMapping("/diary/detail/{plantId}/{diaryId}")
	public String getDetail(Model model, Locale locale, 
			@AuthenticationPrincipal CustomUserDetails user,
			@PathVariable("plantId") Integer plantId, @PathVariable("diaryId") Integer diaryId) {
		
		////ユーザー情報取得、設定
		Integer UserId = user.getId();
        model.addAttribute("UserId", UserId);
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
        
  		//観察日記情報取得
  		Diaries diary = diaryService.getDiary(diaryId);
  		model.addAttribute("diary", diary);
  		// 観察日フォーマット
  		model.addAttribute("observationDateStr", FormatUtils.formatToJapaneseDateString(diary.getObservationDate()));
  		
  		//観察日記画像保存先ディレクトリ設定
  		model.addAttribute("uploadDirDiary", uploadDirDiary);
  		
		return "diary/diary-detail";
	}
}
