package controller;

import command.Command;
import command.CommandFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;
import org.json.JSONTokener;
import util.PagePath;
import util.VKParams;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@WebServlet("/VkServlet")
public class VKServlet extends HttpServlet {
    private static final String COMMAND = "command";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        HttpClient client = new HttpClient();
        PostMethod getAccessTokenMethod = new PostMethod("https://oauth.vk.com/access_token");
        getAccessTokenMethod.addParameter("client_id", VKParams.client_id);
        getAccessTokenMethod.addParameter("client_secret", VKParams.client_secret);
        getAccessTokenMethod.addParameter("redirect_uri", VKParams.redirect_uri);
        getAccessTokenMethod.addParameter("code", code);
        try {
            client.executeMethod(getAccessTokenMethod);
            InputStreamReader reader = new InputStreamReader(getAccessTokenMethod.getResponseBodyAsStream());
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject userResponse = new JSONObject(tokener);
            int user_id = userResponse.getInt("user_id");
            String access_token= userResponse.getString("access_token");
            reader.close();
            PostMethod getInfoMethod = new PostMethod("https://api.vk.com/method/users.get");
            getInfoMethod.addParameter("user_ids", String.valueOf(user_id));
            getInfoMethod.addParameter("v", VKParams.version);
            getInfoMethod.addParameter("access_token", access_token);
            client.executeMethod(getInfoMethod);
            reader = new InputStreamReader(getInfoMethod.getResponseBodyAsStream(), "UTF-8");
            tokener = new JSONTokener(reader);
//            JSONObject userInfoResponse = new JSONObject(tokener);
//            int k =0;
            JSONObject userInfoResponse = new JSONObject(tokener).getJSONArray("response").getJSONObject(0);
            String firstName = userInfoResponse.getString("first_name");
            String lastName = userInfoResponse.getString("last_name");
            request.getSession(true).setAttribute("name", firstName + " " + lastName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher(PagePath.MENU_PATH).forward(request, response);
    }
}