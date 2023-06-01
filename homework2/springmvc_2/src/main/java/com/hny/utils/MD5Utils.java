package com.hny.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class MD5Utils {

    private static final String DOT = ".";

    private static final Map<String, String> HEADERS = new HashMap<>(8);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String KEY = "5edaca6ca2e4d32b7a2a64cbaf9f6e07";

    static {
        HEADERS.put("alg", "HS256");
        HEADERS.put("typ", "JWT");
    }

    public static String getPWD(String strs) {
        /*
         * 加密需要使用JDK中提供的类
         */
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] bs = digest.digest(strs.getBytes());

            /*
             *  加密后的数据是-128 到 127 之间的数字，这个数字也不安全。
             *   取出每个数组的某些二进制位进行某些运算，得到一个具体的加密结果
             *
             *   0000 0011 0000 0100 0010 0000 0110 0001
             *  &0000 0000 0000 0000 0000 0000 1111 1111
             *  ---------------------------------------------
             *   0000 0000 0000 0000 0000 0000 0110 0001
             *   把取出的数据转成十六进制数
             */

            for (byte b : bs) {
                int x = b & 255;
                String s = Integer.toHexString(x);
                if (x > 0 && x < 16) {
                    sb.append("0");
                    sb.append(s);
                } else {
                    sb.append(s);
                }
            }

        } catch (Exception e) {
            System.out.println("加密失败");
        }
        return sb.toString();
    }

    String generateHeaderPart() throws JsonProcessingException {
        byte[] headerBytes = OBJECT_MAPPER.writeValueAsBytes(HEADERS);
        String headerPart = new String(Base64.encodeBase64(headerBytes, false, true), StandardCharsets.US_ASCII);
        System.out.println("生成的Header部分为:" + headerPart);
        return headerPart;
    }

    String generatePayloadPart(Map<String, Object> claims) throws JsonProcessingException {
        byte[] payloadBytes = OBJECT_MAPPER.writeValueAsBytes(claims);
        String payloadPart = new String(Base64.encodeBase64(payloadBytes, false, true), StandardCharsets.UTF_8);
        System.out.println("生成的Payload部分为:" + payloadPart);
        return payloadPart;
    }

    String generateSignaturePart(String headerPart, String payloadPart) {
        String content = headerPart + DOT + payloadPart;
        Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, KEY.getBytes(StandardCharsets.UTF_8));
        byte[] output = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        String signaturePart = new String(Base64.encodeBase64(output, false, true), StandardCharsets.UTF_8);
        System.out.println("生成的Signature部分为:" + signaturePart);
        return signaturePart;
    }

    public String generate(Map<String, Object> claims) throws Exception {
        String headerPart = generateHeaderPart();
        String payloadPart = generatePayloadPart(claims);
        String signaturePart = generateSignaturePart(headerPart, payloadPart);
        String jws = headerPart + DOT + payloadPart + DOT + signaturePart;
        System.out.println("生成的JWT为:{}" + jws);
        return jws;
    }

    public static void main(String[] args) throws Exception {

        Map<String, Object> claims = new HashMap<>(8);
        claims.put("sub", "20231069");
        claims.put("name", "黄南烨");
        claims.put("course", "API设计与实现");
        claims.put("iat", 1516239022);
        MD5Utils md5Utils = new MD5Utils();
        System.out.println("自行生成的JWT:" + md5Utils.generate(claims));
    }
}