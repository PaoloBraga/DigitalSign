package it.castelli.sistemi.main.documentManipulation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class VerifyDocument {
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

    public VerifyDocument(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public boolean verify(FileInputStream fileInputStreamSignature, FileInputStream fileInputStream) throws IOException, SignatureException, InvalidKeyException {

        byte[] sigToVerify = new byte[fileInputStreamSignature.available()];
        fileInputStreamSignature.read(sigToVerify);

        fileInputStreamSignature.close();

        dsa.initVerify(publicKey);

        BufferedInputStream bufin = new BufferedInputStream(fileInputStream);

        byte[] buffer = new byte[1024];
        int len;
        while (bufin.available() != 0) {
            len = bufin.read(buffer);
            dsa.update(buffer, 0, len);
        }
        ;

        bufin.close();

        return dsa.verify(sigToVerify);
    }
}
