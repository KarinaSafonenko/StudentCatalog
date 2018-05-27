<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
</head>
<body>
<form name="update" method="POST" action="/ControllerServlet">
    <input type="hidden" name="command" value="update" />
    <br/>ID:<br/>
    <select name="id">
        <c:forEach items="${requestScope.idSet}" var="id">
            <option value="${requestScope.id}">${requestScope.id}</option>
        </c:forEach>
    </select>
    <br/>Surname:<br/>
    <span itemprop="familyName">
            <input type="text" name="surname"/>
        </span>
    <br/>
    <br/>Name:<br/>
    <span itemprop="givenName">
            <input type="text" name="name"/>
        </span>
    <br/>
    <br/>Patronymic:<br/>
    <span itemprop="additionalName">
            <input type="text" name="patronymic"/>
        </span>
    <br/>
    <br/>Birthday:<br/>
    <span itemprop="birthDate">
            <input type="text" name="birthday"/>
        </span>
    <br/>
    <br/>Sex:<br/>
    <select name="sex">
        <span itemprop="gender">
        <option value="MALE">MALE</option>
        </span>
        <span itemprop="gender">
        <option value="FEMALE">FEMALE</option>
        </span>
    </select>
    </span>
    </div>
    <br>
    <c:if test = "${requestScope.incorrect}">Incorrect parameters</c:if>
    <br/>
    <input type="submit" value="Update"/>
</form>
<br>
<a href="../menu.jsp">Menu</a>
</body>
</html>
