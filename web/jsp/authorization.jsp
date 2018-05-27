<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
</head>
Пользователь: ${sessionScope.name}
<br><br>
<form name="vk" method="POST" action="/ControllerServlet">
    <input type="hidden" name="command" value="vk" />
    <input type="submit" value="VK authorization"/>
</form>
<form name="google" method="POST" action="/ControllerServlet">
    <input type="hidden" name="command" value="google" />
    <input type="submit" value="Google authorization"/>
</form>
</body>
</html>
