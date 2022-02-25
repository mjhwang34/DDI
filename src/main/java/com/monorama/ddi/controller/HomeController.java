package com.monorama.ddi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller 
@RequestMapping("/")
@ApiIgnore
public class HomeController {
	
	@GetMapping("/")
	@ApiOperation(value="�ε��� ������", notes="�ε��� �������̴�.")
	public String index() {
		return "index";
	}
	
	@GetMapping("/en")
	@ApiOperation(value="���� ����", notes="-")
	public String en(Model model) {
		return main("en", model);
	}
	
	@GetMapping("/ko")
	@ApiOperation(value="�ѱ� ����", notes="-")
	public String ko(Model model) {
		return main("ko", model);
	}
	
	@GetMapping("/es")
	@ApiOperation(value="�����ξ� ����", notes="-")
	public String es(Model model) {
		return main("es", model);
	}
	
	public String main(String language, Model model) {
		model.addAttribute("language", language);
		return "main";
	}
	
}
