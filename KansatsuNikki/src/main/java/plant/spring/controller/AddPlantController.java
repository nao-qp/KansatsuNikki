package plant.spring.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AddPlantController {

	//植物追加画面を表示
	@GetMapping("/plant/add")
	public String getAddPlant(Model model, @PathVariable("id") Integer id, Locale locale) {
		
		
		return "";
	}
}
