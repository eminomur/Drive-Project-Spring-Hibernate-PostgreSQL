package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private Integer enabled;

	private String password;

	private String rolename;

	private String username;

	//bi-directional many-to-one association to UserFile
	@OneToMany(mappedBy="user")
	private List<UserFile> userFiles;

	public User() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<UserFile> getUserFiles() {
		return this.userFiles;
	}

	public void setUserFiles(List<UserFile> userFiles) {
		this.userFiles = userFiles;
	}

	public UserFile addUserFile(UserFile userFile) {
		getUserFiles().add(userFile);
		userFile.setUser(this);

		return userFile;
	}

	public UserFile removeUserFile(UserFile userFile) {
		getUserFiles().remove(userFile);
		userFile.setUser(null);

		return userFile;
	}

}