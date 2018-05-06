package org.gotson.nestor.infrastructure.encryption

import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.amazonaws.services.kms.model.DecryptRequest
import com.amazonaws.services.kms.model.EncryptRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.nio.ByteBuffer


private val CHARSET = Charsets.ISO_8859_1

@Profile("!plaincrypt")
@Service
class AwsKmsEncryptionService @Autowired constructor(
        @Value("\${amazon.kms.cmk}") private val keyId: String
) : EncryptionService {
    private val kmsClient = AWSKMSClientBuilder.defaultClient()

    override fun encrypt(plainText: String): String {
        val request = EncryptRequest()
                .withPlaintext(ByteBuffer.wrap(plainText.toByteArray(CHARSET)))
        val cipher = kmsClient.encrypt(request).ciphertextBlob
        return cipher.array().toString(CHARSET)
    }

    override fun decrypt(encryptedText: String): String {
        val request = DecryptRequest()
                .withCiphertextBlob(ByteBuffer.wrap(encryptedText.toByteArray(CHARSET)))
        val plainText = kmsClient.decrypt(request).plaintext
        return plainText.array().toString(CHARSET)
    }
}