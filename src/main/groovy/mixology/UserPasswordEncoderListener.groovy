package mixology

import grails.plugin.springsecurity.SpringSecurityService
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent
import org.springframework.beans.factory.annotation.Autowired
import grails.events.annotation.gorm.Listener
import groovy.transform.CompileStatic

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import java.security.Key
import javax.crypto.KeyGenerator
import java.security.SecureRandom;

@CompileStatic
class UserPasswordEncoderListener {

    static KeyGenerator keyGenerator
    static SecretKey secretKey
    static Cipher cipher
    static {
        cipher = Cipher.getInstance(ALGO);
        keyGenerator = KeyGenerator.getInstance(ALGO)
        keyGenerator.init(128) // block size is 128bits
        secretKey = keyGenerator.generateKey()
    }

    @Autowired
    SpringSecurityService springSecurityService

    @Listener(User)
    void onPreInsertEvent(PreInsertEvent event) {
        encodePasswordConfirmForEvent(event)
        encodePasswordForEvent(event)
    }

    @Listener(User)
    void onPreUpdateEvent(PreUpdateEvent event) {
        encodePasswordConfirmForEvent(event)
        encodePasswordForEvent(event)
    }

    private void encodePasswordConfirmForEvent(AbstractPersistenceEvent event) {
        if (event.entityObject instanceof User) {
            User u = event.entityObject as User
            if (u.password && ((event instanceof PreInsertEvent) || (event instanceof PreUpdateEvent
                    && u.isDirty('password')))) {
                event.getEntityAccess().setProperty('passwordConfirm', encodePasswordConfirm(u.passwordConfirm))
            }
        }
    }

    private void encodePasswordForEvent(AbstractPersistenceEvent event) {
        if (event.entityObject instanceof User) {
            User u = event.entityObject as User
            if (u.password && ((event instanceof PreInsertEvent) || (event instanceof PreUpdateEvent
                    && u.isDirty('password')))) {
                event.getEntityAccess().setProperty('password', encodePassword(u.password))
            }
        }
    }

    private static String encodePasswordConfirm(String passwordConfirm) {
        def passwordConfirmEnc = encrypt(passwordConfirm)
        passwordConfirmEnc
    }

    public static String decodePasswordConfirm(String encrypted) {
        def passwordConfirmDecrypted = decrypt(encrypted)
        passwordConfirmDecrypted
    }

    private static final String ALGO = "AES"
    private static final String ALGO2 = "AES/CBC/PKCS5Padding"
    private static final byte[] keyValue = new byte[] { 'T' as char, 'E' as char, 'S' as char, 'T' as char }
    /**
     * Encrypt a string using AES encryption algorithm.
     *
     * @param pwd the password to be encrypted
     * @return the encrypted string
     */
    public static String encrypt(String password) {
        String encodedPwd = "";
        try {
            byte[] plainTextByte = password.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            encodedPwd = encoder.encodeToString(encryptedByte);
            encodedPwd
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedPwd;

    }

    /**
     * Decrypt a string with AES encryption algorithm.
     *
     * @param encryptedData the data to be decrypted
     * @return the decrypted string
     */
    public static String decrypt(String encrypted) {
        String decodedPWD = "";
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedTextByte = decoder.decode(encrypted);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            decodedPWD = new String(decryptedByte);
            decodedPWD
        } catch (Exception e) {

        }
        return decodedPWD;
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    private String encodePassword(String password) {
        springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}

