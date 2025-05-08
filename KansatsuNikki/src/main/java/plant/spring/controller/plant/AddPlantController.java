package plant.spring.controller.plant;

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
import plant.spring.domain.user.model.PlantFiles;
import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.service.PlantFileService;
import plant.spring.domain.user.service.PlantService;
import plant.spring.domain.user.service.impl.CustomUserDetails;
import plant.spring.form.AddPlantForm;

@Controller
@Slf4j
public class AddPlantController {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PlantService plantService;
	@Autowired
	private PlantFileService plantFileService;
	@Autowired
	private JsonParseService jsonParseService;
	@Autowired
	private SlotConfig slotConfig;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-profile}")
	private String uploadDirProfilel;	//プロフィール画像
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像


	//植物追加画面を表示
	@Authenticated
	@GetMapping("/plant/add")
	public String getAddPlant(Model model, AddPlantForm form, Locale locale) {
		// スロット数上限を設定
		model.addAttribute("slotNum", slotConfig.getMaxSlotCount());
		
		return "plant/add";
	}

	//植物登録処理
	@Authenticated
	@ResponseBody		// JSONで返す
	@PostMapping("/plant/add")
	public ResponseEntity<Map<String, Object>> postAddPlant(Model model,
			@AuthenticationPrincipal CustomUserDetails user, Locale locale,
			@ModelAttribute @Validated AddPlantForm form, BindingResult bindingResult,
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
	        @RequestParam(value = "tempIdList", required = false) List<String> tempIdList,
	        @RequestParam("orderedIds") String orderedIdsJson) {
		
		//入力チェック結果、バリデーションエラーがある場合
		if (bindingResult.hasErrors()) {
			//NG:植物追加画面に戻る
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
		Plants plant = modelMapper.map(form, Plants.class);

		//認証情報からidを取得し、植物データに追加
		plant.setUsersId(user.getId());

		//植物登録
		plantService.addPlant(plant);

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
            PlantFiles newPlantFile = new PlantFiles(plant.getId(), displayOrder, "defaultFileName");
            plantFileService.addPlantFile(newPlantFile);
            
            //// 画像ファイルを保存する
    		//ファイル名を生成して更新（(orderedId)_(plants_id)_yyyyMMdd.jpg）
    		//(INCREMENTで作成されたidでファイル名を作成してupdate)
    		Integer plantFilesId = newPlantFile.getId();
    		StringBuilder sb = new StringBuilder ();
    		sb.append(plantFilesId);
    		sb.append("_");
    		sb.append(plant.getId());
    		sb.append("_");
    		// 日付を取得
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            sb.append(date);
            sb.append(".jpg");
    		String fileName = sb.toString();
    		
            // 保存先のパスを作成
            File destinationFile = new File(Paths.get(uploadStaticDir, uploadDirPlant, fileName).toString());
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
            plantFileService.editPlantFile(plantFilesId, fileName);
            
		    displayOrder++;
	    }
    		
//    	
//        for (MultipartFile file : files) {
//            if (!file.isEmpty()) {
//            	//// 植物画像データを作成する ////
//            	//PlantsテーブルにAUTO_INCREMENTで生成されたidを取得
//        		Integer plantsId = plant.getId();
//
//        		//植物ファイルのインスタンス作成
//        		PlantFiles plantFile = new PlantFiles(plantsId, displayOrder, "defaultFileName");
//        		//画像表示順更新
//        		displayOrder += 1;
//
//        		//植物画像データ登録
//        		//ファイル名は一時的に設定
//        		plantFileService.addPlantFile(plantFile);
//        		
//        		
//        	//// 新規ファイルアップロード ////
//		    	// 新規ファイルリストのMapからファイルを取得
//		        MultipartFile file = tempIdToFileMap.get(orderedId);
//	            // ファイルチェック
//	            FileValidationUtils.validateImageContentType(file);
//	            
//	            // DB登録
//	            // 一時的にファイル名を"defaultFileName"にして登録する(DBのAUTO_INCREMENTで作成されるIDをファイル名で使用するため。)
//	            PlantFiles newPlantFile = new PlantFiles(plant.getId(), displayOrder, "defaultFileName");
//	            plantFileService.addPlantFile(newPlantFile);
//        		
//        		
//        		
//        		
//        		//// 画像ファイルを保存する ////
//        		//ファイル名を生成して更新（(id)_(plants_id)_yyyyMMdd.jpg）
//        		//(INCREMENTで作成されたidでファイル名を作成してupdate)
//        		Integer plantFilesId = plantFile.getId();
//        		StringBuilder sb = new StringBuilder ();
//        		sb.append(plantFilesId);
//        		sb.append("_");
//        		sb.append(plantsId);
//        		sb.append("_");
//        		// 日付を取得
//                String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
//                sb.append(date);
//                sb.append(".jpg");
//        		String fileName = sb.toString();
//        		
//                // 新しいファイルパスを作成
//                File destinationFile = new File(Paths.get(uploadStaticDir, uploadDirPlant, fileName).toString());
//                try {
//                    // ファイルを保存
//                    file.transferTo(destinationFile);
//                    // 反映確認（最大10回まで） アップロード後に画面に画像が表示されない現象防止
////                    int retries = 0;
////                    while (!destinationFile.exists() && retries < 10) {
////                        Thread.sleep(100);
////                        retries++;
////                    }
////	            } catch (IOException | InterruptedException e) {
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	                //NG:エラーレスポンスを返す
//	    			Map<String, Object> errorResponse = new HashMap<>();
//	    	        errorResponse.put("status", "error");
//	    	        // エラーメッセージをセット
//	    	        Map<String, String> errors = new HashMap<>();
//	    	        errors.put("error", "ファイルの保存中にエラーが発生しました。");
//	    	        errorResponse.put("errors", errors);
//	    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//	            }
//
//                //// 植物画像ファイルの名前を更新する ////
//        		//植物画像データ更新
//        		plantFileService.editPlantFile(plantFilesId, fileName);
//
//            }
//        }

      //更新直後のマイページリダイレクト時に更新した画像が取得できないので時間を置く
//        try {
//      	Thread.sleep(3000); // 3秒(3000ミリ秒)間だけ処理を止める
//	    } catch (InterruptedException e) {
//	    }
	
		//登録結果のレスポンスを返す
        Map<String, Object> response = new HashMap<>();
 		response.put("status", "success");
 		response.put("redirectUrl", "/plant/mypage");
	    return ResponseEntity.ok(response);
	}
}
