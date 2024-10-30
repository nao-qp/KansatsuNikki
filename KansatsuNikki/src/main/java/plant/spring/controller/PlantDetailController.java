package plant.spring.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlantDetailController {

	//植物詳細表示
	@GetMapping("/plant/detail/{id}")
	public String getDetail(Model model, Locale locale, @PathVariable("id") Integer id) {
		
		
		
		
		
		return "plant/detail";
	}
	
	
	
}
