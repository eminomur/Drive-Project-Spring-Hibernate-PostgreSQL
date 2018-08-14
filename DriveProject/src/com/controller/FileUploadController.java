package com.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String fileUploadForm() {
		return "fileUploadForm";
	}

	// Handling single file upload request
	@RequestMapping(value = "/singleFileUpload", method = RequestMethod.POST)
	public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
		// Save file on system
		if (!file.getOriginalFilename().isEmpty()) {
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(new File("C:/Users/e_min/Desktop/new", file.getOriginalFilename())));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();

			model.addAttribute("msg", "File uploaded successfully.");
		} else {
			model.addAttribute("msg", "Please select a valid file..");
		}

		return "fileUploadForm";
	}

	// Handling multiple files upload request
	@RequestMapping(value = "/multipleFileUpload", method = RequestMethod.POST)
	public String multipleFileUpload(@RequestParam("file") MultipartFile[] files, Model model) throws IOException {
		// Save file on system
		for (MultipartFile file : files) {
			if (!file.getOriginalFilename().isEmpty()) {
				BufferedOutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(new File("C:/Users/e_min/Desktop/new", file.getOriginalFilename())));

				outputStream.write(file.getBytes());
				outputStream.flush();
				outputStream.close();
			} else {
				model.addAttribute("msg", "Please select at least one file..");
				return "fileUploadForm";
			}
		}
		model.addAttribute("msg", "Multiple files uploaded successfully.");
		return "fileUploadForm";
	}

}
