package plant.spring.controller;

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
	@Autowired
	private PlantFileService plantFileService;
	@Autowired
	private MessageSource messageSource;
	
	//画像ディレクトリ取得
	@Value("${app.upload-static-dir}")
	private String uploadStaticDir;			//ディレクトリ
	@Value("${app.upload-dir-profile}")
	private String uploadDirProfilel;	//プロフィール画像
	@Value("${app.upload-dir-plant}")
	private String uploadDirPlant;		//植物画像
	


	//植物追加画面を表示
	@GetMapping("/plant/add")
	public String getAddPlant(Model model, AddPlantForm form, Locale locale) {
		return "plant/add";
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

		//登録ボタン制御
		//フォーム送信時にボタンに「処理中」と表示する
		String btnProcessing = messageSource.getMessage("btnProcessing", null, locale);
		model.addAttribute("btnProcessing", btnProcessing);
		
		//formをクラスに変換
		Plants plant = modelMapper.map(form, Plants.class);

		//認証情報からidを取得し、植物データに追加
		plant.setUsersId(user.getId());

		//植物登録
		plantService.addPlant(plant);

		

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

      //更新直後のマイページリダイレクト時に更新した画像が取得できないので時間を置く
        try {
      	Thread.sleep(3000); // 3秒(3000ミリ秒)間だけ処理を止める
	    } catch (InterruptedException e) {
	    }
	
		//登録後、植物一覧（マイページ）に遷移
		return "redirect:/plant/mypage";

	}
}
