package plant.spring.controller.diary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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

import lombok.extern.slf4j.Slf4j;
import plant.spring.aop.annotation.Authenticated;
import plant.spring.domain.user.model.Diaries;
import plant.spring.domain.user.model.DiaryFiles;
import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.service.DiaryFileService;
import plant.spring.domain.user.service.DiaryService;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.impl.CustomUserDetails;
import plant.spring.form.AddDiaryForm;

@Controller
@Slf4j
public class AddDiaryController {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PlantService plantService;
	@Autowired
	private DiaryService diaryService;
	@Autowired
	private DiaryFileService diaryFileService;
	@Autowired
	private MessageSource messageSource;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ

	@Value("${app.upload-dir-diary}")
	private String uploadDirDiaryt;		//観察日記画像
	
	//観察日記登録画面表示
	@Authenticated
	@GetMapping("/diary/add/{id}")
	public String getAddDiary(Model model, AddDiaryForm form, @PathVariable("id") Integer id, Locale locale, 
			@AuthenticationPrincipal CustomUserDetails user) {
		
		////認証情報チェック
		// 現在のユーザーの認証情報を取得
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		
//        //認証情報がない場合は、ログインページにリダイレクトする
//        if (authentication == null) {
//        	 return "redirect:/user/login";
//        }
        
        // 認証されたユーザーのIDを取得
//        Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        Integer currentUserId = user.getId();

		//植物情報取得
		Plants plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		
		//該当の植物がログイン中のユーザーのデータかチェック
		if (plant.getUsersId() != currentUserId) {
			//ログイン中のユーザーの植物でない場合は、ログインページへリダイレクト
			 return "redirect:/user/login";
		}
		
		return "diary/add";
	}
	
	
	
	//観察日記登録処理
	@PostMapping("/diary/add/{id}")
	public String postAddDiary(Model model,
	@PathVariable("id") Integer id,
	@AuthenticationPrincipal CustomUserDetails user,
	@RequestParam("files") MultipartFile[] files,
	@ModelAttribute @Validated AddDiaryForm form, BindingResult bindingResult,
	Locale locale) {
		
		//入力チェック結果
		if (bindingResult.hasErrors()) {
			//NG:観察日記追加画面に戻る
			return getAddDiary(model, form, id, locale, user);
		}

		log.info(form.toString());

		//登録ボタン制御
		//フォーム送信時にボタンに「処理中」と表示する
		String btnProcessing = messageSource.getMessage("btnProcessing", null, locale);
		model.addAttribute("btnProcessing", btnProcessing);
		
		//formをクラスに変換
		Diaries diary = modelMapper.map(form, Diaries.class);

		//認証情報からidを取得し、植物データに追加
		diary.setUsersId(user.getId());
		//植物IDをリクエストパラメータから設定
		diary.setPlantsId(id);

		//観察日記登録
		diaryService.addDiary(diary);
		
		// アップロードディレクトリのパスを指定
		String uploadDir = uploadStaticDir + uploadDirDiaryt;
		
		//画像表示順設定
    	int displayOrder = 1;
    	
		//ファイルがある場合
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
            	
            	//// 観察日記画像データを作成する ////
            	//DiariesテーブルにAUTO_INCREMENTで生成されたidを取得
        		Integer diariesId = diary.getId();

        		//観察日記ファイルのインスタンス作成
        		DiaryFiles diaryFile = new DiaryFiles(diariesId, displayOrder, "defaultFileName");
        		//画像表示順更新
        		displayOrder += 1;

        		//植物画像データ登録
        		//ファイル名は一時的に設定
        		diaryFileService.addDiaryFile(diaryFile);
        		
        		
        		//// 画像ファイルを保存する ////
        		//ファイル名を生成して更新（(id)_(plants_id)_yyyyMMdd.jpg）
        		//(INCREMENTで作成されたidでファイル名を作成してupdate)
        		Integer diaryFilesId = diaryFile.getId();
        		StringBuilder sb = new StringBuilder ();
        		sb.append(diaryFilesId);
        		sb.append("_");
        		sb.append(diariesId);
        		sb.append("_");
        		// 日付を取得
                String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
                sb.append(date);
                sb.append(".jpg");
        		String fileName = sb.toString();
        		
                // 新しいファイルパスを作成
                File destinationFile = new File(uploadDir + fileName);
                try {
                    // ファイルを保存
                    file.transferTo(destinationFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "ファイルのアップロード中にエラーが発生しました。");
                    return "error";  // エラーページに遷移する場合
                }

                //// 植物画像ファイルの名前を更新する ////
        		//植物画像データ更新
        		diaryFileService.editDiaryFile(diaryFilesId, fileName);

            }
        }

      //更新直後のマイページリダイレクト時に更新した画像が取得できないので時間を置く
        try {
      	Thread.sleep(3000); // 3秒(3000ミリ秒)間だけ処理を止める
	    } catch (InterruptedException e) {
	    }
		
		
		
		//植物詳細画面へ
		return "redirect:/plant/detail/{id}";
		
	}
	
	
}
