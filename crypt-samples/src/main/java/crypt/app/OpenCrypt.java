package crypt.app;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OpenCrypt {
    public static byte[] getSHA256(String source, String salt) {
        byte[] data = null;
        String algorithm = "SHA-256";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(source.getBytes());
            md.update(salt.getBytes());
            data = md.digest();
            System.out.println("source: " +  source + ", SHA-256: "+ byteArrayToHex(data));
        }catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm: " + algorithm);
            return null;
        }

        return data;
    }

    public static byte[] generateKey(String algorithm) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        return key.getEncoded();
    }

    // generate public and private key for RSA
    public static List<Key> toList(String algorithm) throws NoSuchAlgorithmException {

        KeyPair keyPair = generateKeyPair(algorithm);

        return Arrays.asList(keyPair.getPublic(), keyPair.getPrivate());
    }

    public static KeyPair generateKeyPair(String algorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        return keyPair;
    }

    private static String initVector = "AAAAAAAAAAAAAAAA";
    private static int MAX_IV_LENGTH = 16;

    private static KeyPair rsaKeyPair = null;
    static {
        initVector = shortUUID();
    }

    static {
        try {
            rsaKeyPair = generateKeyPair("RSA");
        } catch (NoSuchAlgorithmException e) {
            // do nothing
        }
    }

    private static String shortUUID() {
        long l = ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong();
        return Long.toString(l, MAX_IV_LENGTH);
    }

    // encrypt method using AES.
    public static String encrypt(String msg, byte[] key) throws Exception {
        Key secretKeySpec = buildAESKey(key);
        Cipher cipher = buildAESCBCPKC5PaddingCipher();
        cipher.init(
                Cipher.ENCRYPT_MODE,
                secretKeySpec,
                new IvParameterSpec(initVector.getBytes())
                );
        byte[] encrypted = cipher.doFinal(msg.getBytes());
        return byteArrayToHex(encrypted);
    }

    public static byte[] encryptRSA(String msg) throws Exception {
        String algorithm = "RSA";
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPrivate());
        byte[] encrypted = cipher.doFinal(msg.getBytes());
        return encrypted;
    }

    public static byte[] decryptRSA(byte[] msg) throws Exception {
        String algorithm = "RSA";
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPublic());
        byte[] decrypted = cipher.doFinal(msg);
        return decrypted;
    }

    public static String decrypt(String msg, byte[] key) throws Exception {
        Key secretKeySpec = buildAESKey(key);
        Cipher cipher = buildAESCBCPKC5PaddingCipher();
        cipher.init(
                Cipher.DECRYPT_MODE,
                secretKeySpec,
                new IvParameterSpec(initVector.getBytes())
        );
        byte[] encrypted = hexToByteArray(msg);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

    public static byte[] hexToByteArray(String hex) {
        return Optional.ofNullable(hex)
                .map(h -> {
                    byte[] ba = new byte[h.length() / 2];
                    for (int i = 0; i < ba.length; i++) {
                        ba[i] = (byte) Integer.parseInt(hex.substring(2* i, 2 * i + 2), 16);
                    }
                    return ba;

                })
                .orElse(null);
    }

    public static String byteArrayToHex(byte[] data) {
        return IntStream.range(0, data.length).map(idx -> data[idx])
                .mapToObj(b -> ("0" + Integer.toHexString(b)))
                .map(s -> s.substring(s.length() - 2)).collect(Collectors.joining());
    }

    private static Key buildKey(byte[] key, String algorithm) {
        return new SecretKeySpec(key, algorithm);
    }

    private static Key buildAESKey(byte[] key) {
        return buildKey(key, "AES");
    }

    private static Key buildRSAKey(byte[] key) {
        return buildKey(key, "RSA");
    }

    private static Cipher buildCipher(String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(transformation);
    }

    private static Cipher buildAESCBCPKC5PaddingCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        String transformation = "AES/CBC/PKCS5Padding";
        return Cipher.getInstance(transformation);
    }
}
