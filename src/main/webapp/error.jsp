<%-- 
    Document   : error
    Created on : Feb 24, 2025, 8:15:41 AM
    Author     : anhkc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
    </head>
    <body>
            <c:if test="${empty fn:trim(pageContext.exception.message) and empty fn:trim(requestScope.errorMessage)}">
                <h2>An error occured:</h2>
                <p>Unknown error</p>
            </c:if>
            <c:if test="${not empty fn:trim(pageContext.exception.message)}">
                <h2>An error occured:</h2>
                <p>${pageContext.exception.message}</p>
            </c:if>
            <c:if test="${not empty fn:trim(requestScope.errorMessage)}">
                <h2>An error occured:</h2>
                <p>${requestScope.errorMessage}</p>
            </c:if>
    </body>
</html>
