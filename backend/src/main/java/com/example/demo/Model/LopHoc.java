package com.example.demo.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table

public class LopHoc {
	public List<HocVien> getHocViens() {
		return hocViens;
	}

	public void setHocViens(List<HocVien> hocViens) {
		this.hocViens = hocViens;
	}

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int lopHocId;
	
	@Column
	private String tenLop;
	
	@Column
	@Max(value = 10, message = "Moi lop toi da 10 hoc vien")
	private int siSo;
	
	@OneToMany(mappedBy = "lopHoc")
	@JsonIgnoreProperties(value ="lopHoc" )
	private List<HocVien> hocViens;
	
	public int getLopHocId() {
		return lopHocId;
	}

	public void setLopHocId(int lopHocId) {
		this.lopHocId = lopHocId;
	}

	public String getTenLop() {
		return tenLop;
	}

	public void setTenLop(String tenLop) {
		this.tenLop = tenLop;
	}

	public int getSiSo() {
		return siSo;
	}

	public void setSiSo(int siSo) {
		this.siSo = siSo;
	}
	
	
	
}
