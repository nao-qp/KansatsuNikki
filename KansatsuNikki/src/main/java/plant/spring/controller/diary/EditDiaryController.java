package plant.spring.controller.diary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import plant.spring.aop.annotation.Authenticated;
import plant.spring.domain.user.model.Diaries;
import plant.spring.domain.user.model.DiaryFiles;
import plant.spring.domain.user.service.DiaryFileService;
import plant.spring.domain.user.service.DiaryService;
import plant.spring.domain.user.service.impl.CustomUserDetails;
import plant.spring.form.AddDiaryForm;

@Controller
public class EditDiaryController {

	@Autowired
	private DiaryService diaryService;
	@Autowired
	private DiaryFileService diaryFileService;
	@Autowired
	private ModelMapper modelMapper;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像
	@Value("${app.upload-dir-diary}")
	private String uploadDirDiary;		//植物画像
	
	//日記詳細画面を表示
	@GetMapping("/diary/edit/{id}")		// 日記ID
	@Authenticated
	public String getEditDiary(Model model, AddDiaryForm form, Locale locale,
			@PathVariable("id") Integer id, @AuthenticationPrincipal CustomUserDetails user) {
		
		// 日記情報取得
  		Diaries diary = diaryService.getDiary(id);
  		model.addAttribute("diary", diary);
  		//取得したdiaryデータをformに変換
		form = modelMapper.map(diary, AddDiaryForm.class);
		model.addAttribute("AddDiaryForm", form);
        
  		// 日記画像取得
  		List<DiaryFiles> diaryFiles = diaryFileService.getDiaryFiles(id);
  		model.addAttribute("diaryFiles", diaryFiles);
  		
  		//画像のリストをスロットの数だけ作成する
		List<String> filePathList = new ArrayList<>();
		int slotNum = 4;
		model.addAttribute("slotNum", slotNum);
		for (int i = 0; i < slotNum; i++) {
			// 各スロットについて設定する画像があるか見ていく
			if (diaryFiles.size() > i) {
				filePathList.add(uploadDirDiary + diaryFiles.get(i).getFilePath());
			} else {
				//画像データがない場合は空のスロットを表示するために空でセットする
				filePathList.add("");
			}
		}
		model.addAttribute("filePathList", filePathList);
		
		return "diary/edit";
	}
}
