package command.impl;

import command.Command;
import controller.Trigger;
import org.json.JSONArray;
import org.json.JSONObject;
import util.GoogleParams;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GoogleCommand implements Command {

    public GoogleCommand(){
        File propertiesFile = new File("D:\\AIPOS\\StudentCatalog\\src\\main\\resources\\properties.json");
        FileReader fileReader;
        try {
            fileReader = new FileReader(propertiesFile);
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder properties = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                properties.append(line);
            }
            JSONObject propertiesJson = new JSONObject(properties.toString()).getJSONObject("google");
            GoogleParams.client_id = propertiesJson.getString("client_id");
            GoogleParams.client_secret = propertiesJson.getString("client_secret");
            JSONArray redirectUrisArray = propertiesJson.getJSONArray("redirect_uris");
            GoogleParams.redirect_uri_google_finished = redirectUrisArray.getString(0);
            GoogleParams.token_uri = propertiesJson.getString("token_uri");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Trigger execute(HttpServletRequest request) {
        return new Trigger(String.format("https://accounts.google.com/o/oauth2/auth?redirect_uri=%s&response_type=code&client_id=%s&scope=%s", GoogleParams.redirect_uri_google_finished, GoogleParams.client_id, GoogleParams.scope), Trigger.TriggerType.REDIRECT);
    }
}
