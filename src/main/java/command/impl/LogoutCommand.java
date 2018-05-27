package command.impl;

import command.Command;
import controller.Trigger;
import util.PagePath;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand  implements Command{
    @Override
    public Trigger execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return new Trigger(PagePath.INDEX_PATH, Trigger.TriggerType.REDIRECT);
    }
}
