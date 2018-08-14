package com.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileUploadController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String fileUploadForm() {
		return "index";
	}

	// Handling single file upload request
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		ModelAndView model = new ModelAndView();
		
		// Save file on system
		if (!file.getOriginalFilename().isEmpty()) {
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(new File("C:/Users/e_min/Desktop/new", file.getOriginalFilename())));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();

			model.addObject("msg", "File uploaded successfully.");
			
		} else {
			model.addObject("msg", "Please select a valid file..");
		}
		
		model.addObject("userName", SecurityContextHolder.getContext().getAuthentication().getName());
		model.setViewName("index");

		return model;
	}

}
