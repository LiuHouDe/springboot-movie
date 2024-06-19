package com.ispan.theater.controller;

import com.google.zxing.WriterException;
import com.ispan.theater.service.QRcodeService;
import com.ispan.theater.util.JsonWebTokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
public class QRcodeController {
    @Autowired
    private QRcodeService qrcodeService;
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @GetMapping("/qrcode/{id}")
    public ResponseEntity<byte[]> getQRcode(@PathVariable int id) throws IOException, WriterException {

        String token = jsonWebTokenUtility.createEncryptedToken(String.valueOf(id), null);
        byte[] qrcode = qrcodeService.generateQRCode(id, token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("image/jpeg"));
        headers.setContentLength(qrcode.length);
        return new ResponseEntity<>(qrcode, headers, HttpStatus.OK);
    }

    @GetMapping("/backstage/orderdetailQrcode")
    public ResponseEntity<?> verifyQRcode(@RequestParam String token, @RequestParam Integer id) throws IOException, WriterException {
        if (!jsonWebTokenUtility.validateEncryptedTime(token.substring(7))) {
            boolean canUse = qrcodeService.verifyQRCode(id);
            System.out.println(canUse);
            if (canUse) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
