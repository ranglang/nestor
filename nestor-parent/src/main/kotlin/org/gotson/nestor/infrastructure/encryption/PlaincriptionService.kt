package org.gotson.nestor.infrastructure.encryption

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("plaincrypt")
@Service
class PlaincriptionService : EncryptionService {
    override fun encrypt(plainText: String): String = plainText
    override fun decrypt(encryptedText: String): String = encryptedText
}