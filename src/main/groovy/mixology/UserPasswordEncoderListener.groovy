package mixology

import grails.plugin.springsecurity.SpringSecurityService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
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

    static final Logger logger = LogManager.getLogger(UserPasswordEncoderListener.class)

    @Autowired
    SpringSecurityService springSecurityService

    @Listener(User)
    void onPreInsertEvent(PreInsertEvent event) {
        logger.info("preInsertEvent occurring")
        encodePasswordConfirmForEvent(event)
        encodePasswordForEvent(event)
    }

    @Listener(User)
    void onPreUpdateEvent(PreUpdateEvent event) {
        logger.info("preUpdateEvent occurring")
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

    private static final byte[] keyValue = new byte[16];
    //private static final byte[] keyValue = new byte[] { 'T' as char, 'E' as char, 'S' as char, 'T' as char }
    /**
     * Encrypt a string using AES encryption algorithm.
     *
     * @param pwd the password to be encrypted
     * @return the encrypted string
     */
    public static String encrypt(String password) {
        String encodedPwd = "";
        try {
            //KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGO)
            //keyGenerator.init(128) // block size is 128bits
            SecretKey secretKey = generateKey()
            logger.info("secretKey generated")
            byte[] plainTextByte = password.getBytes();
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            encodedPwd = encoder.encodeToString(encryptedByte);
            encodedPwd
        } catch (Exception e) {
            logger.error("something went wrong with encryption")
            logger.error("${e.getMessage()}")
            e.printStackTrace()
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
        logger.info("decrypting $encrypted")
        String decodedPWD = "";
        try {
            //KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGO)
            //keyGenerator.init(128) // block size is 128bits
            SecretKey secretKey = generateKey()
            logger.info("secretKey generated")
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedTextByte = decoder.decode(encrypted);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            decodedPWD = new String(decryptedByte);
            logger.info("decoded: $decodedPWD")
            decodedPWD
        } catch (Exception e) {
            logger.error("something went wrong with decryption")
            logger.error("${e.getMessage()}")
            e.printStackTrace()
        }
        return decodedPWD;
    }

    /**
     * Generate a new encryption key.
     */
    private static SecretKey generateKey() {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    private String encodePassword(String password) {
        springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}

