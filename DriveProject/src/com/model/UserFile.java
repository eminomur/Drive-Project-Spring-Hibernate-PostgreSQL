package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_files database table.
 * 
 */
@Entity
@Table(name="user_files")
@NamedQuery(name="UserFile.findAll", query="SELECT u FROM UserFile u")
public class UserFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="file_id")
	private Integer fileId;

	@Column(name="file_name")
	private String fileName;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="owner_id")
	private User user;

	public UserFile() {
	}

	public Integer getFileId() {
		return this.fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}