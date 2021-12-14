package com.example.demo.Controller;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.HocVien;
import com.example.demo.Model.LopHoc;
import com.example.demo.Repository.HocVienRepository;
import com.example.demo.Repository.LopHocRepository;
import com.google.gson.Gson;


@RestController
@RequestMapping(value = "/hocVien")
public class HocVienController {
	
	@Autowired 
	HocVienRepository hocVienRepository;
	
	@Autowired
	LopHocRepository lopHocRepository;
	
	@CrossOrigin
	@GetMapping(value = "/dsHocVien", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HocVien> GetHocVien()	{
			return hocVienRepository.findAll();
	}
	
	public void  CapNhapSiSo(int lopId)	{
		LopHoc lop=lopHocRepository.findById(lopId).get();
		int siSo=0;
//		for(HocVien hv:hocVienRepository.findAll())
//		{
//			if(hv.getLopHoc().getLopHocId()==lopId)	{
//				siSo++;
//			}
//		}
		siSo = (int)hocVienRepository.findAll().stream().filter(x->x.getLopHoc().getLopHocId() == lopId).count();
		lop.setSiSo(siSo);
		ValidatorFactory valFac=Validation.buildDefaultValidatorFactory();
		Validator val=valFac.getValidator();
		Set<ConstraintViolation<LopHoc>> validations=val.validate(lop);
		if(validations.size() ==0)	{
			lopHocRepository.save(lop);
		}
	}
	
	public  boolean KiemTraSiSo(int lopId)	{
		LopHoc lop= lopHocRepository.findById(lopId).get();
		int siSo=lop.getSiSo();
		lop.setSiSo(siSo+1);
		ValidatorFactory valFac=Validation.buildDefaultValidatorFactory();
		Validator val=valFac.getValidator();
		Set<ConstraintViolation<LopHoc>> validations=val.validate(lop);
		if(validations.size()>0)	{
			return  false;
		}
		return  true;

	}

	@CrossOrigin
	@PostMapping(value = "/themHocVien")
	public Object ThemHocVien(@RequestBody String hocVien)	{
		Gson gson= new Gson();
		HocVien hocVienMoi= gson.fromJson(hocVien, HocVien.class);
		
		
		ValidatorFactory valFac=Validation.buildDefaultValidatorFactory();
		Validator val=valFac.getValidator();
		Set<ConstraintViolation<HocVien>> validations=val.validate(hocVienMoi);
		for(ConstraintViolation<HocVien> validation : validations)	{
			System.out.println(validation.getMessage());
		}
		
		Optional<LopHoc> op = Optional.empty();
		if(validations.size()==0&&lopHocRepository.findById(hocVienMoi.getLopHoc().getLopHocId())!=op)	{

//&& hocVienMoi.getLopHoc().getSiSo() >=10
			if(!KiemTraSiSo(hocVienMoi.getLopHoc().getLopHocId()))	{
				return new ResponseEntity<String>(
						"Mỗi lớp không quá 10 học sinh",
						HttpStatus.BAD_REQUEST);
			}
			else
			{
				hocVienRepository.save(hocVienMoi);
				CapNhapSiSo(hocVienMoi.getLopHoc().getLopHocId());
			}
			return "thành công";
		}
		
		return "không thành công";
	}
	
	@CrossOrigin
	@PutMapping(value = "/suaHocVien")
	
	public String SuaHocVien(@RequestBody String hocVien)	{
		Gson gson= new Gson();
		HocVien hocVienMoi=gson.fromJson(hocVien, HocVien.class);
		HocVien hocVienCurrent=hocVienRepository.findById(hocVienMoi.getIdHocVien()).get();
		
		if(hocVienCurrent==null)	{
			return "khong thanh cong";
		}
		
		else	{
			hocVienCurrent.setHoTen(hocVienMoi.getHoTen());
			hocVienCurrent.setDiaChi(hocVienMoi.getDiaChi());
			hocVienCurrent.setNgaySinh(hocVienMoi.getNgaySinh());
			
			
			ValidatorFactory valFac=Validation.buildDefaultValidatorFactory();
			Validator val=valFac.getValidator();
			Set<ConstraintViolation<HocVien>> validations=val.validate(hocVienCurrent);
			for(ConstraintViolation<HocVien> validation : validations)	{
				System.out.println(validation.getMessage());
			}
			
			if(validations.size()==0)	{
				hocVienRepository.save(hocVienCurrent);
				return "thanh cong";
			}
		}
		
		return "khong thanh cong";
	}
	
	@CrossOrigin
	@DeleteMapping(value = "/xoaHocVien")	
	
	public String XoaHS(@RequestParam int idHocVien)	{
		Optional<HocVien> op=Optional.empty();
		HocVien hsx=hocVienRepository.findById(idHocVien).get();
		
		if(hocVienRepository.findById(idHocVien)==op)	{
			return "Học viên không tồn tại";
		}
		else 	{
			int lopId= hsx.getLopHoc().getLopHocId();
			hocVienRepository.deleteById(idHocVien);
			CapNhapSiSo(lopId);
			return "Xoá thàng công";
		}
	}
	
	@CrossOrigin
	@GetMapping(value = "/timKiem", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HocVien> TimKiemHV(@RequestParam String timKiem)	{
		return hocVienRepository.findAll().stream().filter(x -> x.getHoTen().contains(timKiem)).collect(Collectors.toList());
	}
	
	

}
