package plant.spring.controller.diary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import plant.spring.aop.annotation.Authenticated;
import plant.spring.application.service.JsonParseService;
import plant.spring.application.util.FileValidationUtils;
import plant.spring.application.util.ImageUtils;
import plant.spring.config.SlotConfig;
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
	private JsonParseService jsonParseService;
	@Autowired
	private SlotConfig slotConfig;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-diary}")
	private String uploadDirDiary;		//観察日記画像
	
	//観察日記登録画面表示
	@Authenticated
	@GetMapping("/diary/add/{id}")
	public String getAddDiary(Model model, AddDiaryForm form, @PathVariable("id") Integer id, Locale locale, 
			@AuthenticationPrincipal CustomUserDetails user) {
		
		// スロット数上限を設定
		model.addAttribute("slotNum", slotConfig.getMaxSlotCount());
				
        // 認証されたユーザーのIDを取得
        Integer currentUserId = user.getId();

		//植物情報取得
		Plants plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		
		//該当の植物がログイン中のユーザーのデータかチェック
		if (plant.getUsersId() != currentUserId) {
			//ログイン中のユーザーの植物でない場合は、ログインページへリダイレクト
			 return "redirect:/user/login";
		}
		
		return "diary/add-diary";
	}
	
	
	
	//観察日記登録処理
	@Authenticated
	@ResponseBody		// JSONで返す
	@PostMapping("/diary/add/{plantId}")
	public ResponseEntity<Map<String, Object>> postAddDiary(Model model, Locale locale,
	@AuthenticationPrincipal CustomUserDetails user,
	@PathVariable("plantId") Integer plantId,
	@ModelAttribute @Validated AddDiaryForm form, BindingResult bindingResult,
	@RequestParam(value = "files", required = false) List<MultipartFile> files,
	@RequestParam(value = "tempIdList", required = false) List<String> tempIdList,
    @RequestParam("orderedIds") String orderedIdsJson) {
		
		//入力チェック結果
		if (bindingResult.hasErrors()) {
			//NG:観察日記追加画面に戻る
			Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("status", "error");

	        // 各フィールドごとのエラーメッセージを取得
	        Map<String, String> errors = new HashMap<>();
	        bindingResult.getFieldErrors().forEach(error -> 
	            errors.put(error.getField(), error.getDefaultMessage())
	        );
	        errorResponse.put("errors", errors);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}

		log.info(form.toString());

		//formをクラスに変換
		Diaries diary = modelMapper.map(form, Diaries.class);

		//認証情報からidを取得し、植物データに追加
		diary.setUsersId(user.getId());
		//植物IDをリクエストパラメータから設定
		diary.setPlantsId(plantId);

		//観察日記登録
		diaryService.addDiary(diary);	// diaryにAUTO_INCREMENTで作成されたidがセットされる
		
		// 文字列のJSONデータを文字列の配列に変換する
		List<String> orderedIds;	// スロット並び順IDリスト
		try {
			orderedIds = jsonParseService.parseJsonList(orderedIdsJson, "スロット並び順IDリスト");
		} catch (IOException e) {
			e.printStackTrace();
             //NG:エラーレスポンスを返す
 			Map<String, Object> errorResponse = new HashMap<>();
 	        errorResponse.put("status", "error");
 	        // エラーメッセージをセット
 	        Map<String, String> errors = new HashMap<>();
 	        errors.put("error", e.getMessage());
 	        errorResponse.put("errors", errors);
 	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
				}
		
		//// 新規ファイルをアップロード ////
   		// 新規追加画像
   		// tempIdとMultipartFileをマッピングしておく（フロントで設定した一時IDとファイル）
   		Map<String, MultipartFile> tempIdToFileMap = new HashMap<>();
   		if (tempIdList != null) {		// 新規追加画像がない場合は、tempIdListはnull
   			for (int i = 0; i < tempIdList.size(); i++) {
   			    tempIdToFileMap.put(tempIdList.get(i), files.get(i));
   			}
   		}
   		
   		//画像表示順設定
       	int displayOrder = 1;
       	for (String orderedId : orderedIds) {
   	        //// 新規ファイルアップロード ////
   	    	// 新規ファイルリストのMapからファイルを取得
   	        MultipartFile file = tempIdToFileMap.get(orderedId);
           // ファイルチェック
           FileValidationUtils.validateImageContentType(file);
           
           // DB登録
           // 一時的にファイル名を"defaultFileName"にして登録する(DBのAUTO_INCREMENTで作成されるIDをファイル名で使用するため。)
           DiaryFiles newDiaryFile = new DiaryFiles(diary.getId(), displayOrder, "defaultFileName");
               diaryFileService.addDiaryFile(newDiaryFile);
               
            //// 画像ファイルを保存する
       		//ファイル名を生成して更新（(orderedId)_(diaries_id)_yyyyMMdd.jpg）
       		//(INCREMENTで作成されたidでファイル名を作成してupdate)
       		Integer diaryFilesId = newDiaryFile.getId();
       		StringBuilder sb = new StringBuilder ();
       		sb.append(diaryFilesId);
       		sb.append("_");
       		sb.append(diary.getId());
       		sb.append("_");
       		// 日付を取得
               String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
               sb.append(date);
               sb.append(".jpg");
       		String fileName = sb.toString();
       		
               // 保存先のパスを作成
               File destinationFile = new File(Paths.get(uploadStaticDir, uploadDirDiary, fileName).toString());
               try {
               	// 画像を正方形300×300にトリミングリサイズして保存する。
               	ImageUtils.saveResizedImage(file, destinationFile);
               } catch (IOException e) {
               	 e.printStackTrace();
                //NG:エラーレスポンスを返す
       			Map<String, Object> errorResponse = new HashMap<>();
       	        errorResponse.put("status", "error");
       	        // エラーメッセージをセット
       	        Map<String, String> errors = new HashMap<>();
       	        errors.put("error", "ファイルの保存中にエラーが発生しました。");
       	        errorResponse.put("errors", errors);
       	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
               }
               
               // ファイル名更新
               diaryFileService.updDiaryFileName(diaryFilesId, fileName);
               
   		    displayOrder++;
   	    }
		
       	//登録結果のレスポンスを返す
	    Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("redirectUrl", "/plant/detail/" + plantId);
	    return ResponseEntity.ok(response);
	}
}
