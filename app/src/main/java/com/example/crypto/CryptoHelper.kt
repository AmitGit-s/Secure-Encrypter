package com.example.crypto

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object CryptoHelper {
    private const val ITERATIONS = 12000
    private const val KEY_LENGTH = 256
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val CIPHER_ALGORITHM = "AES/GCM/NoPadding"
    private const val SALT_LENGTH = 16
    private const val IV_LENGTH = 12 // Standard for AES-GCM
    private const val TAG_LENGTH = 128 // bits

    /**
     * Encrypts a plaintext message using a password.
     * The output is a Base64 string containing: [16 bytes salt] + [12 bytes IV] + [ciphertext].
     */
    fun encrypt(plainText: String, password: CharArray): String {
        try {
            // 1. Generate random salt
            val salt = ByteArray(SALT_LENGTH)
            SecureRandom().nextBytes(salt)

            // 2. Derive 256-bit AES key from the password using PBKDF2
            val spec = PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)
            val skf = SecretKeyFactory.getInstance(ALGORITHM)
            val derivedKey = skf.generateSecret(spec).encoded
            val secretKey = SecretKeySpec(derivedKey, "AES")

            // 3. Generate random IV
            val iv = ByteArray(IV_LENGTH)
            SecureRandom().nextBytes(iv)

            // 4. Set up Cipher in ENCRYPT_MODE
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            val gcmSpec = GCMParameterSpec(TAG_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)

            // 5. Encrypt
            val plainBytes = plainText.toByteArray(Charsets.UTF_8)
            val cipherText = cipher.doFinal(plainBytes)

            // 6. Combine parts: salt + iv + ciphertext
            val combined = ByteArray(SALT_LENGTH + IV_LENGTH + cipherText.size)
            System.arraycopy(salt, 0, combined, 0, SALT_LENGTH)
            System.arraycopy(iv, 0, combined, SALT_LENGTH, IV_LENGTH)
            System.arraycopy(cipherText, 0, combined, SALT_LENGTH + IV_LENGTH, cipherText.size)

            // 7. Return safe Base64 representation
            return Base64.encodeToString(combined, Base64.NO_WRAP)
        } catch (e: Exception) {
            throw RuntimeException("Encryption failed: ${e.message}", e)
        }
    }

    /**
     * Decrypts a Base64-encoded encrypted payload using a password.
     */
    fun decrypt(encryptedText: String, password: CharArray): String {
        try {
            val combined = Base64.decode(encryptedText.trim(), Base64.NO_WRAP)
            if (combined.size < SALT_LENGTH + IV_LENGTH) {
                throw IllegalArgumentException("Invalid encrypted payload size.")
            }

            // 1. Extract Salt
            val salt = ByteArray(SALT_LENGTH)
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH)

            // 2. Extract IV
            val iv = ByteArray(IV_LENGTH)
            System.arraycopy(combined, SALT_LENGTH, iv, 0, IV_LENGTH)

            // 3. Extract CipherText
            val cipherTextSize = combined.size - SALT_LENGTH - IV_LENGTH
            val cipherText = ByteArray(cipherTextSize)
            System.arraycopy(combined, SALT_LENGTH + IV_LENGTH, cipherText, 0, cipherTextSize)

            // 4. Derive key using Salt from the payload and same PBKDF2 settings
            val spec = PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)
            val skf = SecretKeyFactory.getInstance(ALGORITHM)
            val derivedKey = skf.generateSecret(spec).encoded
            val secretKey = SecretKeySpec(derivedKey, "AES")

            // 5. Set up Cipher in DECRYPT_MODE
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            val gcmSpec = GCMParameterSpec(TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

            // 6. Decrypt
            val decryptedBytes = cipher.doFinal(cipherText)

            return String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            throw IllegalArgumentException("Decryption failed. Please check your password or input.", e)
        }
    }
}
