package Utils;

import APIUtils.requests;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JSONlib {

    JSONParser jsonParser = new JSONParser();
    Environment ev = new Environment();
    Object object;
    String _endpoint, _requestType, _url, _body;
    HashMap<String, String> paramMap = new HashMap<String, String>();
    HashMap<String, String> headerMap = new HashMap<String, String>();

    public requests readFromAPIJson(String server, String endpoint, String type)
    {


        String path = Constants._API_CONFIG;
        path = String.format(path, server);
        try {

            Boolean present=false;
            object = jsonParser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) object;
            JSONObject jsonObject2 = null;
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");


            for (int i = 0; i < jsonArray.size(); i++)
            {
                jsonObject2 = (JSONObject) jsonArray.get(i);
                String val = (String) jsonObject2.get("endpoint");
                if (val.equals(endpoint)) {
                    _endpoint = val;
                    break;
                }
            }
            if(_endpoint==null)
            {
                System.out.println("Endpoint not found");
            }


            String JSONRequestType = (String) jsonObject2.get("requestType");
            if(JSONRequestType.equalsIgnoreCase(type))
            {
                _requestType = JSONRequestType;
            }
            else{
                System.out.println("API method not found");
            }

            _url = ev.readProperties("host");

            present = jsonObject2.containsKey("params");
            if(present==true)
            {

                JSONObject jsonObject4 = (JSONObject) jsonObject2.get("params");

                paramMap = toMap(jsonObject4);
                if(paramMap.isEmpty())
                    System.out.println("Parameters not found !!!!!!!");

            }
            else
                System.out.println("Parameters not defined");

            present = jsonObject2.containsKey("headers");
            if(present==true)
            {
                JSONObject jsonObject4 = (JSONObject) jsonObject2.get("headers");

                headerMap = toMap(jsonObject4);
                if(headerMap.isEmpty())
                    System.out.println("Headers mising");
            }


            present = jsonObject2.containsKey("body");
            if(present==true)
            {
                _body = (String) jsonObject2.get("body");
            }


        }

        catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new requests(_requestType, _url, _body , headerMap, paramMap, _endpoint);
    }
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keySet().iterator();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value.toString());
        }
        return map;
    }

}
