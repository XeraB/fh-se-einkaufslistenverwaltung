package de.fhms.sweng.einkaufslistenverwaltung.inbound.security;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Component
public class PublicKeyProvider {

    private PublicKey publicKey;

    /**
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    @PostConstruct
    protected void loadSigningKeys() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        //read public key
        InputStream inputStream = new ClassPathResource("publicKey").getInputStream();
        byte[] encodedPublicKey = IOUtils.toByteArray(inputStream);

        //encode publicKey
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(publicKeySpec);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}