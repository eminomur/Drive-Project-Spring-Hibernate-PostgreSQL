package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dao.FileDao;

import net.sf.jasperreports.engine.JRException;

@Controller
public class FileController {

	@Autowired
	FileDao fileDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView mainPage() {
		ModelAndView model = new ModelAndView();

		model.addObject("userFileList",
				fileDao.fileList(SecurityContextHolder.getContext().getAuthentication().getName()));
		model.addObject("user", SecurityContextHolder.getContext().getAuthentication().getName());
		model.setViewName("index");

		return model;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView upload(@RequestParam("file") MultipartFile[] files) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean duplicate = false;

		for (MultipartFile file : files) {
			// Save file on system and database
			if (!file.getOriginalFilename().isEmpty()) {
				if (!fileDao.fileUpload(SecurityContextHolder.getContext().getAuthentication().getName(), file)) {
					duplicate = true;
				}
			} else {
				model.addObject("color", "red");
				model.addObject("msg", "Please select a valid file.");
			}
		}

		if (model.isEmpty() && !duplicate) {
			model.addObject("color", "green");
			model.addObject("msg", "Files are uploaded successfully.");
		} else if (duplicate) {
			model.addObject("color", "yellow");
			model.addObject("msg", "Some files are not uploaded. Duplicate files are not allowed");
		}

		model.addObject("user", SecurityContextHolder.getContext().getAuthentication().getName());
		model.addObject("userFileList",
				fileDao.fileList(SecurityContextHolder.getContext().getAuthentication().getName()));
		model.setViewName("index");

		return model;
	}

	@RequestMapping(value = "/delete/{fileId}", method = RequestMethod.GET)
	public String delete(@PathVariable int fileId) {
		fileDao.fileDelete(fileId, SecurityContextHolder.getContext().getAuthentication().getName());
		return "redirect:/";
	}

	@RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
	public String download(HttpSession session, HttpServletResponse response, @PathVariable int fileId)
			throws Exception {
		try {
			String fileName = fileDao.targetFileName(fileId);

			// Change this string with your file location
			String filePathToBeServed = "C:/Users/e_min/Desktop/UserFiles/2/" + fileName;
			File fileToDownload = new File(filePathToBeServed);
			InputStream inputStream = new FileInputStream(fileToDownload);

			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	// Return type must be void otherwise an exception is thrown
	@RequestMapping(value = "/downloadlist/{username}", method = RequestMethod.GET)
	public void report(HttpServletResponse response, @PathVariable String username)
			throws JRException, IOException, SQLException {
		fileDao.reportDownload(response, username);
	}

}
