package it.castelli.sistemi.main.documentManipulation;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

    public String sign(File file) throws InvalidKeyException, IOException, SignatureException {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        dsa.initSign(privateKey);
        byte[] buffer = new byte[1024];
        int len;

        while (bufferedInputStream.available() != 0) {
            len = bufferedInputStream.read(buffer);
            dsa.update(buffer, 0, len);
        };

        bufferedInputStream.close();

        byte[] realSig = dsa.sign();

        return Base64.getEncoder().encodeToString(realSig);
    }
}
