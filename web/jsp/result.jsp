<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head>
</head>
<body>

<table border="1" cellpadding="5" cellspacing="1" >
    <tr>
        <th>id</th>
        <th>initials</th>
        <th>birthday</th>
        <th>sex</th>
    </tr>
    <c:forEach items="${requestScope.studentSet}" var="student" >
        <tr>
            <td>${student.id}</td>
            <span itemscope itemtype="http://schema.org/Person">
                <span itemprop="name"><td>${student.initials}</td></span>
                <span itemprop="birthDate"><td>${student.birthday}</td></span>
                <span itemprop="gender"><td>${student.sex}</td></span>
            </span>
        </tr>
    </c:forEach>
</table>
    <a href="../menu.jsp">Menu</a>
    </br>
</body>
</html>
