package com.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.model.User;
import com.model.UserFile;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@EnableTransactionManagement
@Transactional
@Repository("fileUploadDao")
public class FileDaoImplement implements FileDao {

	@Autowired
	SessionFactory sessionFactory;
	
	final static String directory = "C:/Users/e_min/Desktop/UserFiles/";
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean fileUpload(String username, MultipartFile file, UserFile fileInfo) throws IOException {		
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
			sessionFactory.getCurrentSession().createSQLQuery("insert into cloud.user_files (file_id, file_name, owner_id, topic, keywords) "
					+ "values (nextval('cloud.users_seq'), :filename, :ownerid, :topic, :keywords)")
					.setParameter("filename", file.getOriginalFilename())
					.setParameter("ownerid", list.get(0).getId().intValue())
					.setParameter("topic", fileInfo.getTopic())
					.setParameter("keywords", fileInfo.getKeywords())
					.executeUpdate();
		}
		return true;
	}

	// This method is now only used to find out the size of the list
	@SuppressWarnings("unchecked")
	@Override
	public List<UserFile> fileList(String username) {
		// I could not find more elegant way, if you know one, please tell me
		
		List<User> list = sessionFactory.getCurrentSession()
				.createQuery("from User where username = :username")
				.setParameter("username", username)
				.getResultList();
		
		return sessionFactory.getCurrentSession().createQuery("from UserFile where owner_id = :id order by fileId asc")
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

	@Override
	public void reportDownload(HttpServletResponse response, String username)
			throws SQLException, JRException, IOException {
		InputStream jasperStream = this.getClass().getResourceAsStream("/com/reports/drive report.jasper");
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("user", username);
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
				sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class)
				.getConnection());
		
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=" + username + "filesreport.pdf");
		
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	// This method is now only used to find out the size of the list
	@SuppressWarnings("unchecked")
	@Override
	public List<UserFile> fileSearchByTopic(String username, String keywords) {
		// This is used to find id of user
		List<User> list = sessionFactory.getCurrentSession()
				.createQuery("from User where username = :username")
				.setParameter("username", username)
				.getResultList();
		
		return sessionFactory.getCurrentSession().createQuery("from UserFile where owner_id = :id " +
				"and keywords like :keywords order by fileId asc")
				.setParameter("id", list.get(0).getId().intValue())
				.setParameter("keywords", "%" + keywords + "%")
				.getResultList();
	}

	// To handle page number
	@Override
	public List<Integer> returnPageNumbers(int size) {
		List<Integer> list = new ArrayList<Integer>();
		
		if (size % 10 == 0) {
			size = size / 10;
		} else {
			size = size / 10 + 1;
		}
		
		for (int i = 1; i <= size; ++i) {
			list.add(i);
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserFile> listAccordingToPageNumber(int pageNumber, String username) {
		List<User> list = sessionFactory.getCurrentSession().createQuery("from User where " +
				"username = :username").setParameter("username", username).getResultList();
		
		return sessionFactory.getCurrentSession().createQuery("from UserFile where owner_id = :id " +
				"order by fileId asc")
				.setParameter("id", list.get(0).getId().intValue())
				.setFirstResult((pageNumber - 1) * 10)
				.setMaxResults(10)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserFile> listAccordingToPageNumber(int pageNumber, String username, String keywords) {
		List<User> list = sessionFactory.getCurrentSession().createQuery("from User where username = :username")
				.setParameter("username", username).getResultList();
		
		return sessionFactory.getCurrentSession().createQuery("from UserFile where owner_id = :id " +
				"and keywords like :keywords order by fileId asc")
				.setParameter("id", list.get(0).getId().intValue())
				.setParameter("keywords", "%" + keywords + "%")
				.setFirstResult((pageNumber - 1) * 10)
				.setMaxResults(10)
				.getResultList();
	}

}
