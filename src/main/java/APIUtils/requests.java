package APIUtils;

import java.util.HashMap;

public class requests {

    public String baseURL, payload, query, type;
    public HashMap<String, String> headers, params;

    public void setPayload(String payload) {

        this.payload = payload;
    }

    public void setParams(String key, String value) {

        params.put(key, value);
    }

    public void setHeaders(String key, String value) {

        headers.put(key, value);
    }

    public requests(String type, String baseURL, String payload, HashMap<String, String> headers, HashMap<String, String> params, String query)
    {
        this.type = type;
        this.baseURL = baseURL;
        this.payload = payload;
        this.query = query;
        this.headers = headers;
        this.params = params;
    }
}
