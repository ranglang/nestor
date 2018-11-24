package org.gotson.nestor.infrastructure.encryption.plain

import org.gotson.nestor.infrastructure.encryption.EncryptionService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("!kms")
class PlaincryptionService : EncryptionService {
    override fun encrypt(plainText: String): String = plainText
    override fun decrypt(encryptedText: String): String = encryptedText
}