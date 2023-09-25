package org.keycloak.cli.oidc.oidc.representations;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Map;
import java.util.TreeMap;

@RegisterForReflection
public class JWT {

    @JsonProperty("iss")
    private String iss;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("aud")
    private String aud;

    @JsonProperty("exp")
    private Long exp;

    @JsonProperty("nbf")
    private Long nbf;

    @JsonProperty("iat")
    private Long iat;

    @JsonProperty("jti")
    private String jti;

    protected Map<String, Object> claims = new TreeMap<>();

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getNbf() {
        return nbf;
    }

    public void setNbf(Long nbf) {
        this.nbf = nbf;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    @JsonAnyGetter
    public Map<String, Object> getClaims() {
        return claims;
    }

    @JsonAnySetter
    public void setClaims(String name, Object value) {
        claims.put(name, value);
    }

}
