package APIUtils;

public class responses {
    public int statusCode;
    public String responseJSON;

    public responses(int statusCode, String responseJSON)
    {
        this.statusCode = statusCode;
        this.responseJSON = responseJSON;
    }

}
