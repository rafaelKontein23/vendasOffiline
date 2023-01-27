package visaogrupo.com.br.modulo_visitacao.Views.Models;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Incript {
    static String SECRET_KEY = "oSENHOR3oMeUp4sT0rN4DaMeF4Lt@ra!";
    static String SECRET_IV = "?S4LM0S_23:V3R_1";
    public static String encryptCBC(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec iv = new IvParameterSpec(SECRET_IV.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] crypted = cipher.doFinal(input.getBytes());
        byte[] encodedByte = Base64.encode(crypted, Base64.DEFAULT);
        return new String(encodedByte);
    }

    public static  String decryptCBC(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decodedByte = Base64.decode(str, Base64.DEFAULT);
        IvParameterSpec iv = new IvParameterSpec(SECRET_IV.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        byte[] output = cipher.doFinal(decodedByte);
        return new String(output);
    }


}

