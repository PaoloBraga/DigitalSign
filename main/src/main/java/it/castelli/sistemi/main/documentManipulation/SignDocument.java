package it.castelli.sistemi.main.documentManipulation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;

public class SignDocument {

    PrivateKey privateKey;
    PublicKey publicKey;

    Signature dsa;

    {
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public SignDocument(PrivateKey privateStr, PublicKey publicStr) {
        this.privateKey = privateStr;
        this.publicKey = publicStr;
    }

    public byte[] sign(FileInputStream fileInputStream) throws InvalidKeyException, IOException, SignatureException {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        dsa.initSign(privateKey);
        byte[] buffer = new byte[1024];
        int len;

        while (bufferedInputStream.available() != 0) {
            len = bufferedInputStream.read(buffer);
            dsa.update(buffer, 0, len);
        }

        bufferedInputStream.close();

        return dsa.sign();
    }
}
