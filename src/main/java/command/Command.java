package command;

import controller.Trigger;
import javax.servlet.http.HttpServletRequest;

public interface Command {
    Trigger execute(HttpServletRequest request);
}
