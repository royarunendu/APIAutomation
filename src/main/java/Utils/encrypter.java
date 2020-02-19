package Utils;

import java.util.Base64;

public class encrypter {

    public String decode(String cleartext)
    {
        String decoded = new String(Base64.getDecoder().decode(cleartext));
        return decoded;
    }
}
