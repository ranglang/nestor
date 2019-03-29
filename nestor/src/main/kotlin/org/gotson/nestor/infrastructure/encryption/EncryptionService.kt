package org.gotson.nestor.infrastructure.encryption

interface EncryptionService {
    fun encrypt(plainText: String): String
    fun decrypt(encryptedText: String): String
}