package plant.spring.controller.plant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import plant.spring.aop.annotation.Authenticated;
import plant.spring.application.component.AuthHelper;
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
public class EditPlantController {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PlantService plantService;
	@Autowired
	private PlantFileService plantFileService;
	@Autowired
	private AuthHelper authHelper;
	@Autowired
	private JsonParseService jsonParseService;
	@Autowired
	private SlotConfig slotConfig;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像
	
	
	//植物編集画面を表示
	@Authenticated
	@GetMapping("/plant/edit/{id}")
	public String getEditPlant(Model model, AddPlantForm form, Locale locale,
			@PathVariable("id") Integer id, @AuthenticationPrincipal CustomUserDetails user) {

		// 認証されたユーザーのIDを取得
        Integer currentUserId = user.getId();
        
		//植物情報取得
		Plants plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		
		//取得したplantデータをformに変換
		form = modelMapper.map(plant, AddPlantForm.class);
		model.addAttribute("addPlantForm", form);
		
		//該当の植物がログイン中のユーザーのデータかチェック
		if (!plant.getUsersId().equals(currentUserId)) {
			//ログイン中のユーザーの植物でない場合は、ログインページへリダイレクト
			 return "redirect:/user/login";
		}
		
		//画像データ取得
		List<PlantFiles> plantFiles = plantFileService.getPlantFiles(id);
		// 画面に表示する植物画像のリストを作成
		// 画面表示用のオブジェクトを作成(ファイルIDと表示Urlのセット)
		record fileIdImgUrl(Integer fileId, String imgUrl) {}
		List<fileIdImgUrl> fileIdImgUrlList = new ArrayList<>();
//		List<String> filePathList = new ArrayList<>();
		
		// スロット数上限を設定
		Integer slotNum = slotConfig.getMaxSlotCount();
		model.addAttribute("slotNum", slotNum);
		
		// 画像データ数またはスロット数上限までループする
		int loopCount = Math.min(plantFiles.size(), slotNum);
		for (int i = 0; i < loopCount; i++) {
//			filePathList.add(uploadDirPlant + plantFiles.get(i).getFilePath());
			// 表示Urlを作成
			String imgUrl = uploadDirPlant + plantFiles.get(i).getFilePath();
			// 表示用オブジェクト作成、リストへ追加
			fileIdImgUrlList.add(new fileIdImgUrl(plantFiles.get(i).getId(), imgUrl));
		}
		model.addAttribute("fileIdImgUrlList", fileIdImgUrlList);
		
		return "plant/edit";
	}
	
	//植物編集処理
	@Authenticated
	@PostMapping("/plant/edit/{plantId}")
	public ResponseEntity<Map<String, Object>> postEditPlant(Model model,
			@AuthenticationPrincipal CustomUserDetails user, Locale locale,
			@ModelAttribute @Validated AddPlantForm form, BindingResult bindingResult,
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
	        @RequestParam(value = "tempIdList", required = false) List<String> tempIdList,
	        @RequestParam("orderedIds") String orderedIdsJson,
	        @RequestParam("deletedIds") String deletedIdsJson,
			@PathVariable("plantId") Integer plantId){
		
        //植物情報取得
        Plants plant = plantService.getPlant(plantId);
		model.addAttribute("plant", plant);
		
		//該当の植物がログイン中のユーザーのデータかチェック
		if (!authHelper.isOwner(user, plant)) {
			// ユーザー自身のデータでなければ、ログインページにリダイレクト
		    return authHelper.unauthorizedRedirect();
		}
        
		//入力チェック結果
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
        
		// 編集内容をセットする
		Plants editedPlant = new Plants();
		editedPlant.setId(plantId);
		editedPlant.setName(form.getName());
		editedPlant.setDetail(form.getDetail());
		
		//植物情報を更新
		plantService.editPlant(editedPlant);
		
		// 文字列のJSONデータを文字列の配列に変換する
		List<String> deletedIds;	// 既存ファイルの削除対象Idリスト
		List<String> orderedIds;	// スロット並び順IDリスト
		try {
			deletedIds = jsonParseService.parseJsonList(deletedIdsJson, "削除対象IDリスト");
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
		
		//// 既存ファイルの削除 ////
		if (!deletedIds.isEmpty()) {
			// List<String>からList<Integer>に変換
			List<Integer> integerDeletedIds = deletedIds.stream()
					.filter(s -> s.matches("\\d+"))
					.map(Integer::parseInt)
					.collect(Collectors.toList());
			// ファイル削除処理
			for (Integer integerDeletedId: integerDeletedIds) {
				//// DB削除(is_Deleted更新)
				plantFileService.updIsDeleted(integerDeletedId);
				
				//// 物理画像ファイル削除
				// ファイルIDからファイルパスを取得
				String fileName = plantFileService.getFilePath(integerDeletedId);
				Path filePath = Paths.get(uploadStaticDir, uploadDirPlant, fileName); // 物理ファイルのパス作成
				try {
					// 削除
				    boolean deleted = Files.deleteIfExists(filePath);
				    if (deleted) {
				        System.out.println("ファイル削除成功: " + filePath);
				    } else {
				        System.out.println("ファイルは存在しません: " + filePath);
				    }
				} catch (IOException e) {
				    e.printStackTrace();
				}
			}
		}
		
		//// 新規ファイルをアップロード、既存ファイルの表示順更新 ////
		// 新規追加画像
		// tempIdとMultipartFileをマッピングしておく（フロントで設定した一時IDとファイル）
		Map<String, MultipartFile> tempIdToFileMap = new HashMap<>();
		if (tempIdList != null) {		// 新規追加画像がない場合は、tempIdListはnull
			for (int i = 0; i < tempIdList.size(); i++) {
			    tempIdToFileMap.put(tempIdList.get(i), files.get(i));
			}
		}
		
		// displayOrder順に並べて処理
		int displayOrder = 1;
		for (String orderedId : orderedIds) {
		    if (orderedId.startsWith("temp-")) {
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
	            
		    } else {
		        //// 既存ファイル：DBのdisplayOrder更新 ////
		        Integer plantFileId = Integer.valueOf(orderedId);
		        plantFileService.updateDisplayOrder(plantFileId, plantId, displayOrder);
		    }
    		
		    displayOrder++;
		}
        
        //登録結果のレスポンスを返す
	    Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("redirectUrl", "/plant/mypage");
	    return ResponseEntity.ok(response);
	}
}
