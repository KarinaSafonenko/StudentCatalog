package command.impl;

import command.Command;
import controller.Trigger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;
import org.json.JSONTokener;
import util.GoogleParams;
import util.PagePath;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;

public class FinishGoogleCommand implements Command {
    @Override
    public Trigger execute(HttpServletRequest request) {
        String code = request.getParameter("code");
        try {
            HttpClient client = new HttpClient();
            PostMethod getTokenMethod = new PostMethod(GoogleParams.token_uri);
            getTokenMethod.addParameter("code", code);
            getTokenMethod.addParameter("client_id", GoogleParams.client_id);
            getTokenMethod.addParameter("client_secret", GoogleParams.client_secret);
            getTokenMethod.addParameter("redirect_uri", GoogleParams.redirect_uri_google_finished);
            getTokenMethod.addParameter("grant_type", "authorization_code");
            getTokenMethod.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            client.executeMethod(getTokenMethod);
            JSONObject authResponse = new JSONObject(new JSONTokener(new InputStreamReader(getTokenMethod.getResponseBodyAsStream())));
            String accessToken = authResponse.getString("access_token");
            GetMethod getInfoMethod = new GetMethod("https://www.googleapis.com/oauth2/v1/userinfo?access_token="
                    + accessToken + "&alt=json");
            new HttpClient().executeMethod(getInfoMethod);
            JSONTokener x = new JSONTokener(new InputStreamReader(getInfoMethod.getResponseBodyAsStream(), "UTF-8"));
            JSONObject userInfoJson = new JSONObject(x);
            String name = userInfoJson.getString("name");
            request.getSession(true).setAttribute("name", name);
            System.out.println(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Trigger(PagePath.MENU_PATH, Trigger.TriggerType.FORWARD);
    }
}
