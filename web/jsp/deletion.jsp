<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
</head>
<body>
<form name="deletion" method="POST" action="/ControllerServlet">
    <input type="hidden" name="command" value="delete" />
    <br/>ID:<br/>
    <select name="id">
        <c:forEach items="${requestScope.idSet}" var="id">
            <option value="${requestScope.id}">${requestScope.id}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Delete"/>
</form>
<br>
<a href="../menu.jsp">Menu</a>
</body>
</html>
