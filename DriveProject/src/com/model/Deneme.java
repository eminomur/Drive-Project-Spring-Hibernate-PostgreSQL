package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the deneme database table.
 * 
 */
@Entity
@NamedQuery(name="Deneme.findAll", query="SELECT d FROM Deneme d")
public class Deneme implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private String isim;

	private Integer num;

	public Deneme() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsim() {
		return this.isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}