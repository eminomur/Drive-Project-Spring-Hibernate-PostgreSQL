package com.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.model.User;
import com.model.UserFile;

@EnableTransactionManagement
@Transactional
@Repository("fileUploadDao")
public class FileUploadDaoImplement implements FileUploadDao {

	@Autowired
	SessionFactory sessionFactory;
	
	final static String directory = "C:/Users/e_min/Desktop/UserFiles/";
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean fileUpload(String username, MultipartFile file) throws IOException {		
		List<User> list = sessionFactory.getCurrentSession().createQuery("from User where " +
				"username = :username").setParameter("username", username).getResultList();
	
		if (list.size() > 0) {
			// To add file into target file
			File userFile = new File(directory + list.get(0).getId().intValue());
			
			userFile.mkdirs();
			
			// Checks if file already exists
			// Duplicate files are not allowed
			if (new File(userFile.getAbsolutePath() + "/" + file.getOriginalFilename()).exists()) {
				return false;
			}
			
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(new File(userFile.getAbsolutePath(), file.getOriginalFilename())));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			
			// To add file info into database
			sessionFactory.getCurrentSession().createSQLQuery("insert into cloud.user_files (file_id, file_name, owner_id) "
					+ "values (nextval('cloud.users_seq'), :filename, :ownerid)")
					.setParameter("filename", file.getOriginalFilename())
					.setParameter("ownerid", list.get(0).getId().intValue())
					.executeUpdate();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserFile> fileList(String username) {
		// I could not find more elegant way, if you know one, please tell me
		
		List<User> list = sessionFactory.getCurrentSession()
				.createQuery("from User where username = :username")
				.setParameter("username", username)
				.getResultList();
		
		return sessionFactory.getCurrentSession().createQuery("from UserFile where owner_id = :id")
				.setParameter("id", list.get(0).getId().intValue())
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fileDelete(int fileId, String username) {
		// To delete file from computer
		// Instead of using fileId, using filename is much better in terms of performance I think
		List<User> list = sessionFactory.getCurrentSession().createQuery("from User where username = :username")
				.setParameter("username", username).getResultList();
		List<UserFile> fileList = sessionFactory.getCurrentSession().createQuery("from UserFile where fileId = :id")
				.setParameter("id", fileId).getResultList();
		
		new File(directory + list.get(0).getId().intValue() + "/" + fileList.get(0).getFileName()).delete();
		
		// To delete file from database
		sessionFactory.getCurrentSession().createSQLQuery("delete from cloud.user_files where file_id = :id")
			.setParameter("id", fileId).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String targetFileName(int fileId) {
		List<UserFile> list = sessionFactory.getCurrentSession()
				.createQuery("from UserFile where fileId = :id").setParameter("id", fileId).getResultList();
		return list.get(0).getFileName();
	}

}
