package ua.com.Bershka.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import ua.com.Bershka.Classes.Color;
import ua.com.Bershka.Service.ColorService;
import ua.com.Bershka.dto.filter.SimpleFilter;
import ua.com.Bershka.validator.ColorValidator;
import static ua.com.Bershka.util.ParamBuilder.*;


@Controller
@RequestMapping("/admin/color")
@SessionAttributes("color")

public class ColorController {
	
	@Autowired
	private ColorService colorService;
	
	@ModelAttribute("color")
	public Color getForm(){
		return new Color();
	}
	
	@ModelAttribute("filter")
	public SimpleFilter getFilter(){
		return new SimpleFilter();
	}
	
	@InitBinder("color")
	protected void bind(WebDataBinder binder){
		binder.setValidator(new ColorValidator(colorService));
	}
	
	@GetMapping
	public String show(Model model,@PageableDefault Pageable pageable,@ModelAttribute("filter")SimpleFilter filter){
		model.addAttribute("page",colorService.findAll(filter,pageable));
		return "admin-color";
	}
	
	@GetMapping("update/{id}")
	public String update(@PathVariable int id, Model model,@PageableDefault Pageable pageable,@ModelAttribute("filter")SimpleFilter filter){
		model.addAttribute("color",colorService.findOne(id));
		return show(model,pageable,filter);
		
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable int id,@PageableDefault Pageable pageable,@ModelAttribute("filter")SimpleFilter filter){
		colorService.delete(id);
		return "redirect:/admin/color"+getParams(pageable, filter);
		
	}
	@PostMapping
	public String save(@ModelAttribute("color")@Valid Color color,BindingResult br,Model model,SessionStatus status,@PageableDefault Pageable pageable,@ModelAttribute("filter")SimpleFilter filter){
		if(br.hasErrors()) return show(model,pageable,filter);
		colorService.save(color);
		status.setComplete();
		return "redirect:/admin/color"+getParams(pageable,filter);
	}
}
