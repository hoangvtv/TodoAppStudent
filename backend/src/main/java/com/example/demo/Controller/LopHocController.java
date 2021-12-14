package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/lopHoc")
public class LopHocController {
	@Autowired
	HocVienRepository hocVienRepository;
	
	@Autowired
	LopHocRepository lopHocRepository;
	
	@CrossOrigin
	@GetMapping(value = "/dsLop", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LopHoc> GetDS() {
		return lopHocRepository.findAll();
	}
	
	@CrossOrigin
	@PostMapping(value = "/themLopHoc" )
	public String ThemLopHoc(@RequestBody String lopHoc)	{
		Gson gson = new Gson();
		LopHoc lopHocMoi= gson.fromJson(lopHoc, LopHoc.class);
		
		ValidatorFactory valFac=Validation.buildDefaultValidatorFactory();
		Validator val=valFac.getValidator();
		Set<ConstraintViolation<LopHoc>> validations=val.validate(lopHocMoi);
		for(ConstraintViolation<LopHoc> validation : validations)	{
			System.out.println(validation.getMessage());
		}
		
		if(validations.size()==0)	{
			lopHocRepository.save(lopHocMoi);
			return "Thành công";
		}
		return "Không Thành công";
	}
	
	@CrossOrigin
	@PutMapping(value = "/sualophoc")
	public String SuaLopHoc(@RequestBody String lopHoc)	{
		Gson gson = new Gson();
		LopHoc lopHocMoi=gson.fromJson(lopHoc, LopHoc.class);
		LopHoc lopHocCurrent=lopHocRepository.findById(lopHocMoi.getLopHocId()).get();
		if(lopHocCurrent==null)	{
			return  "không thành công";
		}
		else	{
			lopHocCurrent.setTenLop(lopHocMoi.getTenLop());
			
			
			ValidatorFactory valFac=Validation.buildDefaultValidatorFactory();
			Validator val=valFac.getValidator();
			Set<ConstraintViolation<LopHoc>> validations=val.validate(lopHocCurrent);
			for(ConstraintViolation<LopHoc> validation : validations)	{
				System.out.println(validation.getMessage());
			}
			
			if(validations.size()==0)	{
				lopHocRepository.save(lopHocCurrent);
				return "thanh cong";
			}
		}
		return "khônsg thành công";
	}
	
	@CrossOrigin
	@DeleteMapping(value = "/xoalophoc")
	public String XoaLopHoc(@RequestParam int lopHocId)	{
		Optional<LopHoc> op= Optional.empty();
		LopHoc lopHocXoa=lopHocRepository.findById(lopHocId).get();

		if(lopHocRepository.findById(lopHocId)==op)	{
			return "lớp học không tồn tại";
		}
		else	{
			lopHocXoa.getHocViens().forEach(x -> {

				hocVienRepository.deleteById(x.getIdHocVien());
			});
			lopHocRepository.deleteById(lopHocId);
			return "xóa thành công";
		}
	}
	
	
	
	
}
