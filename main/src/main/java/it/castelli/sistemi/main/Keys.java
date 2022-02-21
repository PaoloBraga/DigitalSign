package it.castelli.sistemi.main;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Keys {
    private String name;
    private String publicKey;
    private String privateKey;
    private PrivateKey prv;
    private PublicKey pub;

    public PrivateKey getPrv() {
        return prv;
    }

    public void setPrv(PrivateKey prv) {
        this.prv = prv;
    }

    public PublicKey getPub() {
        return pub;
    }

    public void setPub(PublicKey pub) {
        this.pub = pub;
    }

    public Keys(String name, byte[] pub, byte[] prv) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.name = name;
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pub);
        this.pub = keyFactory.generatePublic(x509EncodedKeySpec);
        // Bisogna aggiungere la dichiarazione di publicKey e privateKey con la classe Base64
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(prv);
        this.prv = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        publicKey = Base64.getEncoder().encodeToString(this.pub.getEncoded());
        privateKey = Base64.getEncoder().encodeToString(this.prv.getEncoded());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return getName();
    }
}
