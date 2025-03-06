package com.example.dao;

import com.example.entity.Rbao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Repository接口修改
public interface RbaoRepository extends JpaRepository<Rbao, Integer> {
    // 根据学号、日期片段、知识点片段查询
    List<Rbao> findByStunoAndPublishdateContainingAndKnowlegepointsContaining(
            String stuno,
            String publishdate,
            String knowlegepoints
    );


}
