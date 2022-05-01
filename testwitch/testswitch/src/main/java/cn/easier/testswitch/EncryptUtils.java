package cn.easier.testswitch;


import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;


/**
 * 功能描述
 *
 * @author liudo
 * @version [V8.5.70.1, 2021/06/01]
 * @since V8.5.70.1
 */
class EncryptUtils {

    private final static String TAG = EncryptUtils.class.getName();

    private final  String APP_KEY = "a8955b02b5df475882038616d5448d43";

    private final  String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgIwN9mVEWG9kagbxt2ippr8RNzK/fhBXcZa1ViQRnClz3VTjk9cnomIds3AFhsiNihNTPVSirbeCOKxr99mvJuuGdarzfkNEIbOkSLFfO7P6HdQHQjaTg9LueWUy1tz1gh0dsNpg4zPVr+T9lTCTWOnDgU2hNixo0r9wo72dxwXTc55vX4X7sWSz29WzrlKyyBQ2+CcA55EYp6cWwpkaTSfV+Boymr/ZnLI7qlp/7FGZk2574fvE/9uCZdnAHYCTzKOFUjEwZ9o8sw/f+TVglbKvRDSMpqsZXN6DY7FvXMp52ACM7OAp63y8Hir2YKAWj6OJ8KVoS8TAUeDmHyaWwwIDAQAB";

    // rsa 2048
    private  int RSA_PRIVATE_ENCRYPT_MAX_SIZE = 256;


    /**
     * RSA最大加密明文大小
     */
    private  final int MAX_ENCRYPT_BLOCK = 117;


    /**
     * 加密算法RSA
     */
    private  final String KEY_ALGORITHM = "RSA";

    private  final String TYPE_ENCRYRT="RSA/None/PKCS1Padding";

    private static EncryptUtils mInstance;

    private EncryptUtils(){

    }

    public static EncryptUtils getInstance(){
        if(mInstance==null){
            synchronized (EncryptUtils.class){
                if(mInstance==null){
                    mInstance=new EncryptUtils();
                }
            }
        }
        return mInstance;
    }


    String encryptSHA256(Object obj) {
        JSONObject jsonObject = objectToJson(obj);
        TreeMap<String, String> dataOptMap = JsonToMap(jsonObject);
        dataOptMap.put("appKey", APP_KEY);
        //            String key= new String(Base64.decode(KEY.getBytes(), Base64.DEFAULT));
        String signfirst = getUrlParamsByMap(dataOptMap);
//        Log.d(TAG, "encryptSHA256 sign first " + signfirst);
        String sign = getSHA256Str(signfirst).toUpperCase();
//        Log.d(TAG, "encryptSHA256 sign end " + sign);
        return sign;
    }

    private  StringBuffer addParameter(StringBuffer sb, String key, String value) {
        sb.append("&");
        sb.append(key);
        sb.append("=");
        sb.append(value);
        return sb;
    }

    <T> JSONObject objectToJson(T obj) {
        if (null == obj) {
            return new JSONObject();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(obj);
            return new JSONObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    <T> String objectToJsonString(T obj) {
        if (null == obj) {
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    <T> Object JSONToObj(String jsonStr, Class<T> obj) {
        T t = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if(TextUtils.isEmpty(jsonStr)){
                return t;
            }
            t = objectMapper.readValue(jsonStr, obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @param str 加密后的报文
     * @return
     */
    private  String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private  String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


    private  TreeMap<String, String> JsonToMap(JSONObject j) {
        Log.d(TAG, "JsonToMap");
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        if (j == null) {
            Log.e(TAG, "JsonToMap object is null");
            return parameters;
        }
        try {
            Iterator<String> iterator = j.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object obj = j.get(key);
                if (obj == null || TextUtils.isEmpty(obj.toString()) || obj.toString() == " " || obj.toString() == "<null>") {
                    continue;
                }
                String value = obj.toString();
                parameters.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parameters;
    }

    private  String getUrlParamsByMap(Map<String, String> map) {
        Log.d(TAG, "getUrlParamsByMap ");
        if (map == null || map.isEmpty()) {
            Log.e(TAG, "getUrlParamsByMap object is null");
            return "";
        }
        Log.d(TAG, "getUrlParamsByMap  map size " + map.size());
        StringBuffer sb = new StringBuffer();

        for (Map.Entry entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    String encryptByPublicKey(String data) throws Exception {
        return Base64.encodeToString(encryptByPublicKey(data.getBytes(), PUBLIC_KEY),Base64.NO_WRAP);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    private  byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey,Base64.NO_WRAP);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(TYPE_ENCRYRT);
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
}