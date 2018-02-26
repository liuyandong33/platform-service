package build.dream.platform.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class ActivationCodeUtils {
    public static String generateActivationCode() {
        String md5Hex = DigestUtils.md5Hex(UUID.randomUUID().toString()).toUpperCase();
        StringBuilder activationCode = new StringBuilder();
        activationCode.append(md5Hex.substring(0, 4));
        activationCode.append("-");
        activationCode.append(md5Hex.substring(4, 8));
        activationCode.append("-");
        activationCode.append(md5Hex.substring(8, 12));
        activationCode.append("-");
        activationCode.append(md5Hex.substring(12, 16));
        activationCode.append("-");
        activationCode.append(md5Hex.substring(16, 20));
        return activationCode.toString();
    }
}
