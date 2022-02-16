package it.castelli.sistemi.main.documentManipulation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class VerifyDocument {
    PrivateKey privateKey;
    PublicKey publicKey;

    Signature signature;

    public VerifyDocument (PrivateKey privateKey, PublicKey publicKey, Signature signature) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    public boolean verify(File file, File baseFile) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {

        FileInputStream sigfis = new FileInputStream(file);
        byte[] sigToVerify = new byte[sigfis.available()];
        sigfis.read(sigToVerify );

        sigfis.close();

        signature.initVerify(publicKey);

        FileInputStream datafis = new FileInputStream(baseFile);
        BufferedInputStream bufin = new BufferedInputStream(datafis);

        byte[] buffer = new byte[1024];
        int len;
        while (bufin.available() != 0) {
            len = bufin.read(buffer);
            signature.update(buffer, 0, len);
        };

        bufin.close();

        return signature.verify(sigToVerify);
    }
}
