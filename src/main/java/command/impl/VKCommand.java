package command.impl;

import command.Command;
import controller.Trigger;
import org.json.JSONArray;
import org.json.JSONObject;
import util.VKParams;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

import static util.VKParams.client_id;

public class VKCommand implements Command {

    public VKCommand() {
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
            JSONObject propertiesJson = new JSONObject(properties.toString()).getJSONObject("vk");
            client_id = propertiesJson.getString("client_id");
            JSONArray redirectUrisArray = propertiesJson.getJSONArray("redirect_uris");
            VKParams.redirect_uri = redirectUrisArray.getString(0);
            VKParams.version = propertiesJson.getString("version");
            VKParams.scope = propertiesJson.getString("scope");
            VKParams.client_secret = propertiesJson.getString("client_secret");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Trigger execute(HttpServletRequest request) {
        return new Trigger(String.format("https://oauth.vk.com/authorize?client_id=%s&display=page&redirect_uri=%s&scope=%s&response_type=code&v=%s",
                client_id, VKParams.redirect_uri, VKParams.scope, VKParams.version), Trigger.TriggerType.REDIRECT);
    }
}
