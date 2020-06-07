package APIUtils;

import Utils.Environment;
import Utils.JSONlib;
import Utils.encrypter;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.util.HashMap;

public class requestCreater {

    protected String server;
    protected String endpoint;
    encrypter encrypt = new encrypter();
    Environment ev = new Environment();

    public requestCreater()
    {
    }

    public requestCreater(String server, String endPoint)
    {
        this.server=server;
        this.endpoint=endPoint;
    }


    public requests createRequest(methods type) throws ParseException {

        JSONlib jsonlib = new JSONlib();

        requests request = jsonlib.readFromAPIJson(server, endpoint, type.name());

        if(request == null)
        {
            throw new ParseException(0);

        }

        return request;


    }

    public responses callAPI(requests request)
    {
        String baseURL = request.baseURL;
        HashMap<String, String> reqHeaders = request.headers;
        HashMap<String, String> reqParams = request.params;
        String Endpint = request.query;
        String payload = request.payload;
        Response resp=null;
        String token=null;
        try {
           // String temp = ev.readProperties("")
             token= encrypt.decode(ev.readProperties("apiToken"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        RestAssured.baseURI = baseURL;
        RequestSpecification httprequest = RestAssured.given().relaxedHTTPSValidation()
                .header("Authorization", "Bearer "+token);

        methods typeReq = methods.valueOf(request.type.toUpperCase());

        if(reqHeaders!=null) {
            httprequest = httprequest.headers(reqHeaders);
        }

        if(reqParams!=null)
        {
            httprequest = httprequest.queryParams(reqParams);
        }

        switch(typeReq)
        {
            case POST:
                resp = httprequest.post(Endpint);
                break;

            case GET:
                resp =httprequest.get(Endpint);
                break;

            default:
                break;
        }

       responses apiResponse = new responses(resp.statusCode(), resp.body().print());

        System.out.println("Response Status Code: "+apiResponse.statusCode);
        System.out.println("API Response : "+apiResponse.responseJSON);
        return apiResponse;
    }
}
