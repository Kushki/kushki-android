package com.kushkipagos.android;

import android.util.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AurusEncryption {

    private static final int CHUNK_SIZE = 117;
    private final Cipher cipher;

    public AurusEncryption() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC81t5iu5C0JxYq5/XNPiD5ol3Zw8rw3LtFIUm7y3m8o8wv5qVnzGh6XwQ8LWypdkbBDKWZZrAUd3lybZOP7/82Nb1/noYj8ixVRdbnYtbsSAbu9PxjB7a/7LCGKsugLkou74PJDadQweM88kzQOx/kzAyVbS9gCCVUguHcq2vRRQIDAQAB";
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(publickey));
    }

    public String encryptMessageChunk(String requestMessage) throws BadPaddingException, IllegalBlockSizeException {
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (index < requestMessage.length()) {
            String messageChunk = getNextChunk(requestMessage, index);
            byte[] encryptedChunk = encrypt(messageChunk);
            String encodedChunk = Base64.encodeToString(encryptedChunk, Base64.DEFAULT).replace("\n", "");
            stringBuilder.append(encodedChunk);
            stringBuilder.append("<FS>");
            index += CHUNK_SIZE;
        }
        return stringBuilder.toString();
    }

    private String getNextChunk(String requestMessage, int index) {
        int endIndex = Math.min(index + CHUNK_SIZE, requestMessage.length());
        return requestMessage.substring(index, endIndex);
    }

    private byte[] encrypt(String message) throws BadPaddingException, IllegalBlockSizeException {
        byte[] eMessageBytes = message.getBytes(Charset.forName("UTF-8"));       // Request message conversion to Byte array
        return cipher.doFinal(eMessageBytes);
    }

    private PublicKey loadPublicKey(String stored) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] data = Base64.decode(stored, Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }
}