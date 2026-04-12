package org.plain.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * @author Hugh
 */
public class JwtUtil {

    public static Map<String,Object> getClaims(){


        return new HashMap<>();
    }
}
