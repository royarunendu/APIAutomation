package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {


        public String readProperties(String key) throws IOException
        {
            return readProperties(Constants._ENV_PROPERTY, key);
        }


        public String readProperties(String propertyFilePath, String key) throws IOException {
            Properties prop = new Properties();
            InputStream input = null;

            try {
                input = new FileInputStream(propertyFilePath);
                prop.load(input);
                return prop.getProperty(key);

            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
}
