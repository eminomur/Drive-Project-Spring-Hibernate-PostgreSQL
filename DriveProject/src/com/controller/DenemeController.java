package com.controller;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DenemeController {

	@Autowired
	SessionFactory sessionFactory;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView mainPage() {
		return new ModelAndView("index");
	}
}
