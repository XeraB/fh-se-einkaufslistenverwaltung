package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.security.JwtValidator;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.security.PublicKeyProvider;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class JwtValidatorTest {

    @InjectMocks
    JwtValidator jwtValidator;

    @Mock
    PublicKeyProvider keys;

    private static final String TEST_USER_EMAIL = "test1@test.com";

    private static final String TEST_JWT = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUB0ZXN0LmNvbSIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNjYyNDA1OTM0LCJleHAiOjM2MDAxNjYyNDA1OTM0fQ.LuRnt1TN7rRFnSgIhh7LVD8VOxLg5WclSWoNlKwf_67S3LCNL0tJ5W2P-CIY8kcgyG2cnCi4Upd7gBBydrb2luhzRgjUKCjfFhemKHQwVyWtWQ5UexzkAIcr8Y7B9PExy92K5jnuJzcoBrgL5_3lN1v_PE6F2gCMQ0W7a2GDca5u7W1gM3FiQ_93dVWHl8259sTCML05L2gfOD7ImmpJQwpE3NyXThPpzzXUKOiSRtMppEYwIptmnHgmpwH18kWBk0vutHPNxcu-mVkj1o4Tztsnn0qQZnjmmoVbsXjxmPDUF9AQpmHdXz2H1PcFkxyS5oaixjKMGUp6em59VuKz_A";

    private static PublicKey TEST_PUBLIC_KEY;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.keys = mock(PublicKeyProvider.class);
        this.jwtValidator = new JwtValidator(keys);
        InputStream inputStream = new ClassPathResource("publicKey").getInputStream();
        byte[] encodedPublicKey = IOUtils.toByteArray(inputStream);

        //encode publicKey
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.TEST_PUBLIC_KEY = keyFactory.generatePublic(publicKeySpec);
    }

    @Test
    void isValidJWT() {
        when(keys.getPublicKey()).thenReturn(TEST_PUBLIC_KEY);

        assertThat(jwtValidator.isValidJWT(TEST_JWT), is(true));
    }

    @Test
    void isValidJWTException() {
        when(keys.getPublicKey()).thenReturn(TEST_PUBLIC_KEY);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            jwtValidator.isValidJWT(TEST_JWT + "wrong");
        });
    }

    @Test
    void getUserEmail() {
        when(keys.getPublicKey()).thenReturn(TEST_PUBLIC_KEY);

        String result = jwtValidator.getUserEmail(TEST_JWT);
        assertThat(result, is(TEST_USER_EMAIL));
    }
}
