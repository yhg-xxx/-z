package com.example.controller;


import com.example.dao.XiaoxiRepository;
import com.example.entity.Rbao;
import com.example.entity.Xiaoxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class XiaoxiController {
    @Autowired
    private XiaoxiRepository xiaoxiRepository;

    @PostMapping("/xiaoxi")
    public Xiaoxi createXiaoxi(@RequestBody Xiaoxi xiaoxi) {
        return xiaoxiRepository.save(xiaoxi);
    }
    @GetMapping("/xiaoxi/x")
    public List<Xiaoxi> getAllXiaoxis() {
        return xiaoxiRepository.findAll();
    }
    @PostMapping("/updateState1/{id}")
    public ResponseEntity<Map<String, Object>> updateState(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        try {
            Xiaoxi existingXiaoxi = xiaoxiRepository.findById(id).orElse(null);
            if (existingXiaoxi == null) {
                response.put("success", false);
                response.put("message", "消息未找到");
                return ResponseEntity.status(404).body(response);
            }

            // 从请求体中获取state字段
            String newState = requestBody.get("state");
            existingXiaoxi.setState(newState);
            xiaoxiRepository.save(existingXiaoxi);

            response.put("success", true);
            response.put("message", "状态更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
