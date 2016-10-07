package com.kushkipagos.android;

import android.util.Base64;

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

class AurusEncryption {

    private static final int CHUNK_SIZE = 117;
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC81t5iu5C0JxYq5/XNPiD5ol3Z" +
            "w8rw3LtFIUm7y3m8o8wv5qVnzGh6XwQ8LWypdkbBDKWZZrAUd3lybZOP7/82Nb1/" +
            "noYj8ixVRdbnYtbsSAbu9PxjB7a/7LCGKsugLkou74PJDadQweM88kzQOx/kzAyV" +
            "bS9gCCVUguHcq2vRRQIDAQAB";

    String encryptMessageChunk(String requestMessage) throws BadPaddingException, IllegalBlockSizeException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(PUBLIC_KEY));
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (index < requestMessage.length()) {
            String messageChunk = getNextChunk(requestMessage, index);
            byte[] encryptedChunk = encrypt(messageChunk, cipher);
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

    private byte[] encrypt(String message, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException {
        byte[] eMessageBytes = message.getBytes(Charset.forName("UTF-8"));
        return cipher.doFinal(eMessageBytes);
    }

    private PublicKey loadPublicKey(String stored) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] data = Base64.decode(stored, Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }
}