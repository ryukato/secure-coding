package crypt.app;

import java.security.KeyPair;
import java.security.PrivateKey;

public class OpenCryptMain {
    public static void main(String[] args) throws Exception {
        String testPinNo = "abcdefg";
        byte[] key = OpenCrypt.generateKey("AES");
        String encrypted = OpenCrypt.encrypt(testPinNo, key);
        System.out.println(encrypted);
        String decrypted = OpenCrypt.decrypt(encrypted, key);
        System.out.println(decrypted);

        byte[] rsaEncryptedByte = OpenCrypt.encryptRSA(testPinNo);
        byte[] rsaDecryptedByte = OpenCrypt.decryptRSA(rsaEncryptedByte);
        System.out.println(new String(OpenCrypt.byteArrayToHex(rsaEncryptedByte)));
        System.out.println(new String(rsaDecryptedByte));
    }
}
