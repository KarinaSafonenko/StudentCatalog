<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
</head>
Добро пожаловать, ${sessionScope.name}!
<br>
<form name="addition" method="POST" action="/jsp/addition.jsp">
    <input type="hidden" name="command" value="add" />
    <input type="submit" value="Add student"/>
</form>
<form name="deletion" method="POST" action="/ControllerServlet">
    <input type="hidden" name="command" value="prepare_update" />
    <input type="submit" value="Update student"/>
</form>
<form name="deletion" method="POST" action="/ControllerServlet">
    <input type="hidden" name="command" value="prepare_delete" />
    <input type="submit" value="Delete student"/>
</form>
<form name="registration" action="/ControllerServlet">
    <input type="hidden" name="command" value="show" />
    <input type="submit" value="Student list"/>
</form>
<form name="registration" action="/ControllerServlet">
    <input type="hidden" name="command" value="logout" />
    <input type="submit" value="Logout"/>
</form>
<jsp:useBean id = "date" class = "java.util.Date"/>
<p>The date/time is <%= date %></>
</body>
</html>
