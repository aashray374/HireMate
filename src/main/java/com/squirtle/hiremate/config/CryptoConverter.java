package com.squirtle.hiremate.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;

@Converter
public class CryptoConverter  implements AttributeConverter<String,String> {

    @Value("${jasypt.encryptor.password}")
    private static String ENCRYPTION_PASSWORD_PROPERTY;
    private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public CryptoConverter() {
        encryptor.setPassword(ENCRYPTION_PASSWORD_PROPERTY);
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptor.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptor.decrypt(s);
    }
}
