package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

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
import com.model.UserFile;

import net.sf.jasperreports.engine.JRException;

@Controller
public class FileController {

	@Autowired
	FileDao fileDao;
	
	// If you have a better idea, please inform me.
	// This is used to prevent calling functions again and again.
	String username;
	// This is used to save keywords. So we can use it when needed.
	String currentKeywords;
	// This is used to hold number of files
	int numOfElements;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView mainPage() {
		ModelAndView model = new ModelAndView();
		username = SecurityContextHolder.getContext().getAuthentication().getName();
		numOfElements = fileDao.fileList(username).size();
		
		// It shows first page by default
		model.addObject("userFileList", fileDao.listAccordingToPageNumber(1, username));
		model.addObject("user", username);
		model.addObject("size", fileDao.returnPageNumbers(numOfElements));
		model.addObject("keywords", "");
		model.setViewName("index");

		return model;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView upload(@RequestParam("file") MultipartFile[] files, UserFile fileInfo) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean duplicate = false;

		for (MultipartFile file : files) {
			// Save file on system and database
			if (!file.getOriginalFilename().isEmpty()) {
				if (!fileDao.fileUpload(SecurityContextHolder.getContext().getAuthentication().getName(),
						file, fileInfo)) {
					duplicate = true;
				} else {
					++numOfElements;
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

		model.addObject("user", username);
		model.addObject("userFileList", fileDao.listAccordingToPageNumber(1, username));
		model.addObject("size", fileDao.returnPageNumbers(numOfElements));
		model.addObject("keywords", "");
		model.setViewName("index");

		return model;
	}

	@RequestMapping(value = "/delete/{fileId}", method = RequestMethod.GET)
	public String delete(@PathVariable int fileId) {
		fileDao.fileDelete(fileId, username);
		--numOfElements;
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
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView searchFile(UserFile fileInfo) {
		ModelAndView model = new ModelAndView();
		currentKeywords = fileInfo.getKeywords();
		List<UserFile> list;
		
		if (fileInfo.getKeywords() == "") {
			list = fileDao.listAccordingToPageNumber(1, username);
			numOfElements = fileDao.fileList(username).size();
		} else {
			list = fileDao.listAccordingToPageNumber(1, username, fileInfo.getKeywords());
			numOfElements = fileDao.fileSearchByTopic(username, fileInfo.getKeywords()).size();
		}
		
		model.addObject("userFileList", list);
		model.addObject("user", username);
		model.addObject("size", fileDao.returnPageNumbers(numOfElements));
		model.addObject("keywords", currentKeywords);
		model.setViewName("index");
		
		return model;
	}
	
	// To handle pagination staff
	@RequestMapping(value = "/go", method = RequestMethod.GET)
	public ModelAndView page(int pagenum) {
		ModelAndView model = new ModelAndView();
		List<UserFile> list;
		
		if (currentKeywords == "" || currentKeywords == null) {
			list = fileDao.listAccordingToPageNumber(pagenum, username);
		} else {
			list = fileDao.listAccordingToPageNumber(pagenum, username, currentKeywords);
		}
		
		model.addObject("userFileList", list);
		model.addObject("user", username);
		model.addObject("size", fileDao.returnPageNumbers(numOfElements));
		model.addObject("keywords", currentKeywords);
		model.setViewName("index");
		
		return model;
	}

}
