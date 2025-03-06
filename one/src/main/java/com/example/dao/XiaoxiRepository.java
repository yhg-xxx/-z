package com.example.dao;

import com.example.entity.Xiaoxi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XiaoxiRepository extends JpaRepository<Xiaoxi,Integer> {
}
