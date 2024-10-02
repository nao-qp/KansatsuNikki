package plant.spring.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class AddPlantController {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PlantService plantService;
	
	private PlantFileService plantFileService;

    @Value("${app.upload-dir-plant}")
    private String uploadDirPlant;
	
	
	//植物追加画面を表示
	@GetMapping("/plant/add")
	public String getAddPlant(Model model, AddPlantForm form, Locale locale) {
		return "plant/addplant";
	}

	//植物登録処理
	@PostMapping("/plant/add")
	public String postAddPlant(Model model,
			@AuthenticationPrincipal CustomUserDetails user,
			@RequestParam("files") MultipartFile[] files,
			@ModelAttribute @Validated AddPlantForm form, BindingResult bindingResult,
			Locale locale) {
		
		//入力チェック結果
		if (bindingResult.hasErrors()) {
			//NG:植物追加画面に戻る
			return getAddPlant(model, form, locale);
		}
		
		log.info(form.toString());
		
		//formをクラスに変換
		Plants plant = modelMapper.map(form, Plants.class);
		
		//認証情報からidを取得し、植物データに追加
		plant.setUsersId(user.getId());
		
		//植物登録
		plantService.addPlant(plant);

		//PlantsテーブルにAUTO_INCREMENTで生成されたidを取得
		Integer plantsId = plant.getId();
		
		
		////植物画像データ登録処理
		//植物ファイルのインスタンス作成
		PlantFiles plantFile = new PlantFiles(plantsId, 1, "defaultFileName");

		//植物画像データ登録
		//ファイル名は一時的に設定
		plantFileService.addPlantFile(plantFile);

		//ファイル名を生成して更新（(id)_(plants_id)_yyyyMMdd.jpg）
		StringBuilder sb = new StringBuilder ();
		sb.append(plantFile.getId());
		sb.append("_");
		sb.append(plantsId);
		sb.append("_");
		// 日付を取得
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        sb.append(date);
        sb.append(".jpg");
		String fileName = sb.toString();
		
		//植物画像データ更新
		
		
		
		
		
		
		
		// アップロードディレクトリのパスを指定
		String uploadDir = uploadDirPlant;
    
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // 日付を取得
                String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
                //ファイル名を設定（(id)_(plants_id)_yyyyMMdd.jpg）
                String newFileName = "99_99_" + date;
                
                // 新しいファイルパスを作成
                File destinationFile = new File(uploadDir + newFileName);
                try {
                    // ファイルを保存
                    file.transferTo(destinationFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "ファイルのアップロード中にエラーが発生しました。");
                    return "error";  // エラーページに遷移する場合
                }
            }
        }

		
		
		
		
		//登録後、植物一覧（マイページ）に遷移
		return "redirect:/plant/mypage";
	}
}
