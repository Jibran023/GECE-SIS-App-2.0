package com.example.gece_sisapp20

import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val FIXED_IV = "1234567890123456" // Fixed IV (16 bytes)

    @Throws(Exception::class)
    fun decryptJson(encryptedData: String, secretKey: String): String {
        // Decode the Base64 encoded data
        val data = Base64.decode(encryptedData, Base64.DEFAULT)
        Log.d("Decoded data", "Decoded Data: ${bytesToHex(data)}")

        // Extract the IV from the beginning of the data
        val iv = FIXED_IV.toByteArray(StandardCharsets.UTF_8)
        System.arraycopy(data, 0, iv, 0, iv.size)
        val encryptedBytes = ByteArray(data.size - iv.size)
        System.arraycopy(data, iv.size, encryptedBytes, 0, encryptedBytes.size)

        // Prepare the key and IV
        val secretKeySpec = SecretKeySpec(secretKey.padEnd(32, '0').toByteArray(StandardCharsets.UTF_8), ALGORITHM)
        val ivParameterSpec = IvParameterSpec(iv)

        // Initialize the cipher for decryption
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

        // Decrypt the data
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, StandardCharsets.UTF_8)

    }

    @Throws(NoSuchAlgorithmException::class)
    private fun getSHA256Key(key: String): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(key.toByteArray(StandardCharsets.UTF_8))
    }

    // Utility function to convert byte array to hex string
    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

}