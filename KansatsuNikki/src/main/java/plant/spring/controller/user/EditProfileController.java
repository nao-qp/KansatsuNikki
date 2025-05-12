package plant.spring.controller.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import plant.spring.aop.annotation.Authenticated;
import plant.spring.application.util.ImageUtils;
import plant.spring.domain.user.model.Profiles;
import plant.spring.domain.user.service.ProfileService;
import plant.spring.domain.user.service.impl.CustomUserDetails;
import plant.spring.form.EditProfileForm;

@Controller
@Slf4j
public class EditProfileController {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProfileService profileService;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-profile}")
	private String uploadDirProfile;		//プロフィール画像
		
	
	// プロイフィール編集画面表示
	@Authenticated
	@GetMapping("/user/edit-profile/{userId}")
	public String getEditProfile(Model model, EditProfileForm form, Locale locale,
			@PathVariable("userId") Integer userId, @AuthenticationPrincipal CustomUserDetails user) {
		
		// ユーザーID
		model.addAttribute("userId", userId);
		
		// プロフィール情報取得
		Profiles profile = profileService.getProfile(userId);
		model.addAttribute("profile", profile);
		
		// 取得したプロフィールデータをformに変換
		form = modelMapper.map(profile, EditProfileForm.class);
		model.addAttribute("EditProfileForm", form);
		
		// プロフィール画像保存先ディレクトリ設定
		model.addAttribute("uploadDirProfile", uploadDirProfile);

		return "user/edit-profile";
	}

	
	// プロフィール更新処理
	@Authenticated
	@PostMapping("/user/edit-profile/{userId}")
	public ResponseEntity<Map<String, Object>> postEditPlant(Model model,
			@AuthenticationPrincipal CustomUserDetails user, Locale locale,
			@ModelAttribute @Validated EditProfileForm form, BindingResult bindingResult,
			@RequestParam(value = "file", required = false) MultipartFile file,
	        @RequestParam(value = "tempFilePath", required = false) String tempFilePath,
	        @RequestParam("deletedFilePaths") String deletedFilePaths,
			@PathVariable("userId") Integer userId){
		
        //プロフィール情報取得
        Profiles profile = profileService.getProfile(userId);
		
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
		Integer profileId = profile.getId();
		// プロフィール情報(ニックネーム、プロフィール)を更新
		profileService.updNicknameProfile(profileId, form.getNickname(), form.getProfile());
		
		//// 既存ファイルの削除 ////
		if (!deletedFilePaths.isEmpty()) {
			// プロフィールのファイルパスをクリア更新
			profileService.updFilePathClear(profileId);
			
			//// 物理画像ファイル削除
			Path filePath = Paths.get(uploadStaticDir, uploadDirProfile, deletedFilePaths); // 物理ファイルのパス作成
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
		
		// 新しいファイルを選択している場合(通常file自体は送られてくるので、空かどうかチェック必須)
		if (file != null && !file.isEmpty()) {
			//// 新規ファイルをアップロード ////
			//// 画像ファイルを保存する
			//ファイル名を生成して更新（(profileId)_(userId)_yyyyMMdd.jpg）
			StringBuilder sb = new StringBuilder ();
			sb.append(profileId);
			sb.append("_");
			sb.append(userId);
			sb.append("_");
			// 日付を取得
	        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
	        sb.append(date);
	        sb.append(".jpg");
			String fileName = sb.toString();
			
	        // 保存先のパスを作成
	        File destinationFile = new File(Paths.get(uploadStaticDir, uploadDirProfile, fileName).toString());
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
			
			// 新規追加画像ファイルパス更新
			profileService.updFilePath(profileId, fileName);
		}
		
        //登録結果のレスポンスを返す
	    Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("redirectUrl", "/plant/mypage");
	    return ResponseEntity.ok(response);
	}
	
}
