package com.example.controller;

import com.example.dao.RbaoRepository;
import com.example.dao.UserRepository;
import com.example.entity.Rbao;
import com.example.entity.User;
import com.example.util.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RbaoController {
    @Autowired
    private RbaoRepository rbaoRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/rbao")
    public Rbao createRbao(@RequestBody Rbao rbao) {
        return rbaoRepository.save(rbao);
    }

    @DeleteMapping("/rbao/{id}")
    public Integer deleteRbao(@PathVariable int id) {
        try {
            rbaoRepository.deleteById(id);
            return 1; // 删除成功返回1
        } catch (EmptyResultDataAccessException e) {
            // 捕获删除不存在的记录异常
            return 0; // 删除失败返回0
        } catch (Exception e) {
            // 其他异常（如数据库连接失败）
            return 0;
        }
    }

    @PutMapping("/rbao/{id}")
    public Integer updateRbao(@PathVariable int id, @RequestBody Rbao rbao) {
        try {
            // 检查ID是否存在
            if (rbaoRepository.existsById(id)) {
                rbao.setId(id);
                rbaoRepository.save(rbao);
                return 1; // 更新成功返回1
            } else {
                return 0; // ID不存在返回0
            }
        } catch (Exception e) {
            return 0; // 其他异常
        }
    }

    @GetMapping("/rbao/x")
    public List<Rbao> getAllRbaos() {
        return rbaoRepository.findAll();
    }

    @GetMapping("/rbao/personal")
    public ResponseEntity<List<Rbao>> getPersonalRbao(
            @RequestParam String userId,      // 新增用户ID参数
            @RequestParam String publishdate,
            @RequestParam String knowlegepoints) {

        // 直接使用前端传递的用户ID
        List<Rbao> result = rbaoRepository.findByStunoAndPublishdateContainingAndKnowlegepointsContaining(
                userId,          // 使用前端传入的用户ID
                publishdate,
                knowlegepoints
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/updateState/{id}")
    public ResponseEntity<Map<String, Object>> updateState(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        try {
            Rbao existingRbao = rbaoRepository.findById(id).orElse(null);
            if (existingRbao == null) {
                response.put("success", false);
                response.put("message", "日报未找到");
                return ResponseEntity.status(404).body(response);
            }

            // 从请求体中获取state字段
            String newState = requestBody.get("state");
            existingRbao.setState(newState);
            rbaoRepository.save(existingRbao);

            response.put("success", true);
            response.put("message", "状态更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    // 评分更新接口
    @PutMapping("/updateScore")
    public ResponseEntity<String> updateScore(@RequestBody Map<String, Object> payload) {
        return updateDailyField(payload, "score");
    }

    // 评语更新接口
    @PutMapping("/updateComment")
    public ResponseEntity<String> updateComment(@RequestBody Map<String, Object> payload) {
        return updateDailyField(payload, "comment");
    }

    private ResponseEntity<String> updateDailyField(Map<String, Object> payload, String type) {
        try {
            // 参数校验
            if (!payload.containsKey("id")) {
                return ResponseEntity.badRequest().body("缺少日报ID参数");
            }

            int id = (Integer) payload.get("id");
            Optional<Rbao> optionalRbao = rbaoRepository.findById(id);

            if (optionalRbao.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Rbao rbao = optionalRbao.get();

            // 根据类型更新不同字段
            switch (type) {
                case "score":
                    rbao.setDailyscore(payload.get("dailyscore").toString());
                    break;
                case "comment":
                    rbao.setStatereason((String) payload.get("statereason"));
                    break;
                default:
                    return ResponseEntity.badRequest().body("无效的更新类型");
            }

            rbaoRepository.save(rbao);
            return ResponseEntity.ok().body("更新成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("服务器错误: " + e.getMessage());
        }
    }



}



        // 根据id更新数据库中的日报状态