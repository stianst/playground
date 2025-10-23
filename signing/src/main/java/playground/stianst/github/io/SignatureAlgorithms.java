package playground.stianst.github.io;

import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.jcajce.spec.SLHDSAParameterSpec;
import org.bouncycastle.pqc.jcajce.spec.FalconParameterSpec;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

public enum SignatureAlgorithms {

    ES256("SHA256withECDSA", "EC", new ECGenParameterSpec("secp256r1")),
    ES384("SHA384withECDSA", "EC", new ECGenParameterSpec("secp384r1")),
    ES512("SHA512withECDSA", "EC", new ECGenParameterSpec("secp521r1")),
    FALCON_512("Falcon", "Falcon", FalconParameterSpec.falcon_512),
    FALCON_1024("Falcon", "Falcon", FalconParameterSpec.falcon_1024),
    ML_DSA_44("ML-DSA-44", NamedParameterSpec.ML_DSA_44),
    ML_DSA_65("ML-DSA-65", NamedParameterSpec.ML_DSA_65),
    ML_DSA_87("ML-DSA-87", NamedParameterSpec.ML_DSA_87),
    RS256("SHA256withRSA", "RSA", new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)),
    RS384("SHA384withRSA", "RSA", new RSAKeyGenParameterSpec(3072, RSAKeyGenParameterSpec.F4)),
    RS512("SHA384withRSA", "RSA", new RSAKeyGenParameterSpec(4096, RSAKeyGenParameterSpec.F4)),
    SLH_DSA_128s("SLHDSA", NISTObjectIdentifiers.id_slh_dsa_sha2_128s.getId(), SLHDSAParameterSpec.slh_dsa_sha2_128s),
    SLH_DSA_128f("SLHDSA", NISTObjectIdentifiers.id_slh_dsa_sha2_128f.getId(), SLHDSAParameterSpec.slh_dsa_sha2_128f);

    private final String algorithm;
    private final String keyPairAlgorithm;
    private final AlgorithmParameterSpec spec;
    private final String provider;

    SignatureAlgorithms(String algorithm, AlgorithmParameterSpec spec) {
        this.algorithm = algorithm;
        this.keyPairAlgorithm = algorithm;
        this.spec = spec;
        this.provider = null;
    }

    SignatureAlgorithms(String algorithm, String keyPairAlgorithm, AlgorithmParameterSpec spec) {
        this.algorithm = algorithm;
        this.keyPairAlgorithm = keyPairAlgorithm;
        this.spec = spec;
        this.provider = null;
    }

    SignatureAlgorithms(String algorithm, String keyPairAlgorithm, AlgorithmParameterSpec spec, String provider) {
        this.algorithm = algorithm;
        this.keyPairAlgorithm = keyPairAlgorithm;
        this.spec = spec;
        this.provider = provider;
    }

    public String getName() {
        return name().replace('_', '-');
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getKeyPairAlgorithm() {
        return keyPairAlgorithm;
    }

    public AlgorithmParameterSpec getSpec() {
        return spec;
    }

    public String getProvider() {
        return provider;
    }
}
