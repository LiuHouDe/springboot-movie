package com.ispan.theater.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ispan.theater.domain.OrderDetail;
import com.ispan.theater.repository.OrderDetailRepository;
import com.ispan.theater.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRcodeService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    public byte[] generateQRCode(Integer orderId, String token) throws WriterException, IOException {
        String url = "http://localhost:8082/backstage/orderdetailQrcode?id=" + orderId + "&token=Bearer " + token;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 350, 350);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] bytes = pngOutputStream.toByteArray();

        return bytes;
    }
    @Transactional
    public boolean verifyQRCode(Integer id)  {

        OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
        System.out.println(orderDetail);
        System.out.println(orderDetail.getUsed());
        if(!orderDetail.getUsed()) {
            orderDetail.setUsed(true);
            orderDetailRepository.save(orderDetail);
            return true;
        }
        return false;
    }
}
