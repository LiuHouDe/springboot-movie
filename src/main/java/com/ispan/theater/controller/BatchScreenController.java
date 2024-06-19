package com.ispan.theater.controller;

import com.ispan.theater.dto.SchduleDto;
import com.ispan.theater.service.BatchScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class BatchScreenController {
    @Autowired
    private BatchScreenService batchScreenService;
    @PostMapping("/admin/backstage/screening-batch")
    public ResponseEntity<?> createBatchSchedule(@RequestBody SchduleDto request) {
        try {
            batchScreenService.batchScreenInsert(request);
            return ResponseEntity.ok("批量場次新增完成.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("批量新增失敗: " + e.getMessage());
        }
    }
}
