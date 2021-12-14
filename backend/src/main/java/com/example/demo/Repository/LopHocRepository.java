package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.LopHoc;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, Integer> {

}
