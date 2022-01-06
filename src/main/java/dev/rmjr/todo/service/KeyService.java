package dev.rmjr.todo.service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

@Service
public class KeyService {

    @Value("${jwt.private-key}")
    private String privateKey;

    public KeyPair getKeyPair() {
        try {
            Security.addProvider(new BouncyCastleProvider());

            PEMParser parser = new PEMParser(Files.newBufferedReader(Paths.get(privateKey)));
            KeyPair keyPair = new JcaPEMKeyConverter().setProvider("BC").getKeyPair((PEMKeyPair) parser.readObject());

            parser.close();

            return keyPair;
        } catch(IOException e) {
            throw new RuntimeException("Error reading the token keys: " + e.getMessage(), e.getCause());
        }
    }
}
