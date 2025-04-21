package plant.spring.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	private MessageSource messageSource;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像
	
	
	//植物編集画面を表示
	@GetMapping("/plant/edit/{id}")
	public String getEditPlant(Model model, AddPlantForm form, Locale locale,
			@PathVariable("id") Integer id, @AuthenticationPrincipal CustomUserDetails user) {
		
		////認証情報チェック
        //認証情報がない場合は、ログインページにリダイレクトする
        if (user == null) {
        	 return "redirect:/user/login";
        }
        
        // 認証されたユーザーのIDを取得
        Integer currentUserId = user.getId();
        
		//植物情報取得
		Plants plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		
		//取得したplantデータをformに変換
		form = modelMapper.map(plant, AddPlantForm.class);
		model.addAttribute("addPlantForm", form);
		
		//該当の植物がログイン中のユーザーのデータかチェック
		if (plant.getUsersId() != currentUserId) {
			//ログイン中のユーザーの植物でない場合は、ログインページへリダイレクト
			 return "redirect:/user/login";
		}
		
		//画像データ取得
		List<PlantFiles> plantFiles = plantFileService.getPlantFiles(id);
		//植物画像のリストをスロットの数だけ作成する
		List<String> filePathList = new ArrayList<>();
		
		int slotNum = 4;
		model.addAttribute("slotNum", slotNum);
		for (int i = 0; i < slotNum; i++) {
			if (plantFiles.size() > i) {
				filePathList.add(uploadDirPlant + plantFiles.get(i).getFilePath());
			} else {
				//画像データがない場合は空のスロットを表示するために空でセットする
				filePathList.add("");
			}
		}
		model.addAttribute("filePathList", filePathList);
		
		return "plant/edit";
	}
	
	//植物編集処理
	@PostMapping("/plant/edit/{id}")
	public String postEditPlant(Model model,
			@AuthenticationPrincipal CustomUserDetails user, Locale locale,
			@RequestParam("files") MultipartFile[] files,
			@ModelAttribute @Validated AddPlantForm form, BindingResult bindingResult,
			@PathVariable("id") Integer id){
		
		////認証情報チェック
        //認証情報がない場合は、ログインページにリダイレクトする
        if (user == null) {
        	 return "redirect:/user/login";
        }
        
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
        
		//入力チェック結果
		if (bindingResult.hasErrors()) {
			//NG:植物編集画面に戻る
			return getEditPlant(model, form, locale, id, user);
		}

		log.info(form.toString());
        
		//登録ボタン制御
		//フォーム送信時にボタンに「処理中」と表示する
		String btnProcessing = messageSource.getMessage("btnProcessing", null, locale);
		model.addAttribute("btnProcessing", btnProcessing);
		
		//formをクラスに変換
		Plants editedPlant = modelMapper.map(form, Plants.class);
		//編集内容に植物IDを追加
		editedPlant.setId(id);
		
		//植物情報を更新
		plantService.editPlant(editedPlant);
		
		
		
		// アップロードディレクトリのパスを指定
				String uploadDir = uploadStaticDir + uploadDirPlant;
				
				//画像表示順設定
		    	int displayOrder = 1;
		    	
				//ファイルがある場合
		        for (MultipartFile file : files) {
		            if (!file.isEmpty()) {
		            	
		            	//// 植物画像データを作成する ////
		            	//PlantsテーブルにAUTO_INCREMENTで生成されたidを取得
		        		Integer plantsId = plant.getId();

		        		//植物ファイルのインスタンス作成
		        		PlantFiles plantFile = new PlantFiles(plantsId, displayOrder, "defaultFileName");
		        		//画像表示順更新
		        		displayOrder += 1;

		        		//植物画像データ登録
		        		//ファイル名は一時的に設定
		        		plantFileService.addPlantFile(plantFile);
		        		
		        		
		        		//// 画像ファイルを保存する ////
		        		//ファイル名を生成して更新（(id)_(plants_id)_yyyyMMdd.jpg）
		        		//(INCREMENTで作成されたidでファイル名を作成してupdate)
		        		Integer plantFilesId = plantFile.getId();
		        		StringBuilder sb = new StringBuilder ();
		        		sb.append(plantFilesId);
		        		sb.append("_");
		        		sb.append(plantsId);
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
		        		plantFileService.editPlantFile(plantFilesId, fileName);

		            }
		        }
		
		return "redirect:/plant/mypage";
	}
}
