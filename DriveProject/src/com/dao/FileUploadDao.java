package com.dao;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.model.UserFile;

public interface FileUploadDao {

	List<UserFile> fileList(String username);
	boolean fileUpload(String username, MultipartFile file) throws IOException;
	void fileDelete(int fileId, String username);
	String targetFileName(int fileId);
	
}
