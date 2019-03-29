package org.gotson.nestor.infrastructure.encryption.plain

import org.gotson.nestor.infrastructure.encryption.EncryptionService
import org.springframework.stereotype.Service

@Service
class PlaincryptionService : EncryptionService {
    override fun encrypt(plainText: String): String = plainText
    override fun decrypt(encryptedText: String): String = encryptedText
}