package com.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.model.UserFile;

import net.sf.jasperreports.engine.JRException;

public interface FileDao {

	List<UserFile> fileList(String username);
	boolean fileUpload(String username, MultipartFile file, UserFile fileInfo) throws IOException;
	void fileDelete(int fileId, String username);
	String targetFileName(int fileId);
	void reportDownload(HttpServletResponse response, String username) throws SQLException, JRException, IOException;
	List<UserFile> fileSearchByTopic(String username, String keywords);
	List<Integer> returnPageNumbers(int size);
	List<UserFile> listAccordingToPageNumber(int pageNumber, String username);
	List<UserFile> listAccordingToPageNumber(int pageNumber, String username, String keywords);
	Integer getUserId(String username);
	
}
