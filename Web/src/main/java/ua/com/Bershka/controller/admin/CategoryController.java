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

import ua.com.Bershka.Classes.Category;
import ua.com.Bershka.Classes.Sex;
import ua.com.Bershka.Editor.SexEditor;
import ua.com.Bershka.Service.CategoryService;
import ua.com.Bershka.Service.SexService;
import ua.com.Bershka.dto.filter.SimpleFilter;
import ua.com.Bershka.validator.CategoryValidator;
import static ua.com.Bershka.util.ParamBuilder.*;

@Controller
@RequestMapping("/admin/category")
@SessionAttributes("category")
public class CategoryController  {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private SexService sexService;
	
	@ModelAttribute("category")
	public Category getForm(){
		return new Category();
	}
	
	@InitBinder("category")
	protected void bind(WebDataBinder binder){
		binder.registerCustomEditor(Sex.class, new SexEditor(sexService));
		binder.setValidator(new CategoryValidator(categoryService));
	}
	
	@GetMapping
	public String show(Model model){
		model.addAttribute("categories",categoryService.findAll());
		model.addAttribute("sexs",sexService.findAll());
		return "admin-category";
	}
	@GetMapping("delete/{id}")
	public String delete(@PathVariable int id) {
		categoryService.delete(id);
		return "redirect:/admin/category";
		
	}
	
	@GetMapping("update/{id}")
	public String update(@PathVariable int id, Model model){
		model.addAttribute("category",categoryService.findOne(id));
		return show(model);
	}
	@PostMapping
	public String save(@ModelAttribute("category")@Valid Category category,BindingResult br,Model model,SessionStatus status){
		if(br.hasErrors()) return show(model);
		categoryService.save(category);
		status.setComplete();
		return "redirect:/admin/category";
	}
}
