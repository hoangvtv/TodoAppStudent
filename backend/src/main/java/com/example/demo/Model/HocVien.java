package com.example.demo.Model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table
public class HocVien {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idHocVien;
	
	@Column
	@Size(max=100, message = "hoTen khong qua 100 ky tu")
	private String hoTen;
	
	@Column
	private Date ngaySinh;
	
	@Column
	private String diaChi;
	
	@Column
	@Size(max=10, message = "SDT co 10 ky tu")
	private String sDT;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="lopHocId", foreignKey = @ForeignKey(name="fk_hocvien_khoahoc"))
	@JsonIgnoreProperties(value ="hocViens" )
	private LopHoc lopHoc;

	public int getIdHocVien() {
		return idHocVien;
	}

	public LopHoc getLopHoc() {
		return lopHoc;
	}

	public void setLopHoc(LopHoc lopHoc) {
		this.lopHoc = lopHoc;
	}

	public void setIdHocVien(int idHocVien) {
		this.idHocVien = idHocVien;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getsDT() {
		return sDT;
	}

	public void setsDT(String sDT) {
		this.sDT = sDT;
	}
	
	
	
}
