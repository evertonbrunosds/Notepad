package github.evertonbrunosds.notepad.security.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Supplier;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SymmetricSecureProcessor {

    private final String algorithm;

    private final String transformation;

    private final byte[] key;

    public Access<String> encode(final String target) {
        return new Access<String>() {

            @Override
            public <T extends Throwable> String orThrow(final Supplier<T> supplier) throws T {
                try {
                    return internalEncode(target);
                } catch (final Throwable throwable) {
                    throw supplier.get();
                }
            }

        };
    }

    public Access<String> decode(final String target) {
        return new Access<String>() {

            @Override
            public <T extends Throwable> String orThrow(final Supplier<T> supplier) throws T {
                try {
                    return internalDecode(target);
                } catch (final Throwable throwable) {
                    throw supplier.get();
                }
            }

        };
    }

    private String internalDecode(final String target) throws Throwable {
        final SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        final Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        final byte[] decodedBytes = Base64.getDecoder().decode(target);
        final byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private String internalEncode(final String target) throws Throwable {
        final SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        final Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        final byte[] encryptedBytes = cipher.doFinal(target.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
