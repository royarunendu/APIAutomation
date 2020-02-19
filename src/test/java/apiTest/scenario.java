package apiTest;

import APIUtils.methods;
import APIUtils.requestCreater;
import APIUtils.requests;
import APIUtils.responses;
import io.restassured.path.json.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.Random;

public class scenario {

    private String Channelid = null;
    Random rd = new Random();
    int x = rd.nextInt(9000) + 1000;
    int y = rd.nextInt(9000) + 1000;
    String val1 = Integer.toString(x);
    String name = "test_"+val1;
    String val2 = Integer.toString(y);
    String newName = "rename_test_"+val2;

    JSONParser jsonParser = new JSONParser();
    Object object;

    @Test(priority = 1)
    public void post_createChannel() throws ParseException {
        requestCreater req = new requestCreater("slack", "channels.create");
        requests apiRequest = req.createRequest(methods.POST);
        apiRequest.setParams("name",name);
        responses response = req.callAPI(apiRequest);
        Assert.assertEquals(200,response.statusCode);
        JsonPath output = new JsonPath(response.responseJSON);
        Channelid = output.get("channel.id");
        System.out.println("Channel id : "+Channelid);
    }

    @Test(priority = 2)
    public void post_joinChannel() throws ParseException {
        requestCreater req = new requestCreater("slack", "channels.join");
        requests apiRequest = req.createRequest(methods.POST);
        apiRequest.setParams("name",name);
        responses response = req.callAPI(apiRequest);
        Assert.assertEquals(200,response.statusCode);
        JsonPath output = new JsonPath(response.responseJSON);
        Assert.assertEquals(name,output.get("channel.name"),"Not connected to desired channel..!!!");

    }

    @Test(priority = 3)
    public void post_renameChannel() throws ParseException {
        requestCreater req = new requestCreater("slack", "conversations.rename");
        requests apiRequest = req.createRequest(methods.POST);
        apiRequest.setParams("channel",Channelid);
        apiRequest.setParams("name",newName);
        responses response = req.callAPI(apiRequest);
        Assert.assertEquals(200,response.statusCode);
        JsonPath output = new JsonPath(response.responseJSON);
        Assert.assertEquals(newName,output.get("channel.name"),"Channel name not updated..!!!");
    }

    @Test(priority = 4)
    public void get_channels() throws ParseException {
        requestCreater req = new requestCreater("slack", "channels.list");
        requests apiRequest = req.createRequest(methods.GET);
        responses response = req.callAPI(apiRequest);
        Assert.assertEquals(200,response.statusCode);

        JSONObject jsonObject = (JSONObject)jsonParser.parse(response.responseJSON);
        JSONArray jsonArray = (JSONArray) jsonObject.get("channels");

        Boolean flag = false;
        for(int i=0;i<jsonArray.size();i++)
        {
            JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
            System.out.println((i+1)+" : "+jsonObject2.get("name").toString());
            if(jsonObject2.get("name").toString().equals(newName));
            {
                flag = true;
            }
        }
        Assert.assertTrue(flag,"Rename did not happen");

    }

    @Test(priority = 5)
    public void post_archiveChannel() throws ParseException {
        requestCreater req = new requestCreater("slack", "channels.archive");
        requests apiRequest = req.createRequest(methods.POST);
        apiRequest.setParams("channel",Channelid);
        responses response = req.callAPI(apiRequest);
        Assert.assertEquals(200, response.statusCode);
        JsonPath output = new JsonPath(response.responseJSON);
        String value = output.get("ok").toString();
        Assert.assertEquals("true",value,"Failed to archive");
    }
    @Test(priority = 6)
    public void get_channelsAfterArchived() throws ParseException {
        requestCreater req = new requestCreater("slack", "channels.list");
        requests apiRequest = req.createRequest(methods.GET);
        responses response = req.callAPI(apiRequest);
        Assert.assertEquals(200,response.statusCode);
        JSONObject jsonObject = (JSONObject)jsonParser.parse(response.responseJSON);
        JSONArray jsonArray = (JSONArray) jsonObject.get("channels");

        Boolean flag = false;
        for(int i=0;i<jsonArray.size();i++)
        {
            JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
            System.out.println((i+1)+" : "+jsonObject2.get("name").toString());
            if(!jsonObject2.get("name").toString().equals(newName));
            {
                flag = true;
            }
        }
        Assert.assertTrue(flag,"Archived Unsuccessfull");
    }
}
