<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<%@ page session="false" %>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<center>
    <h3>Login to use Word Search Service</h3>
    <form:form name="login" action="" method="POST" modelAttribute="user">
        <div>
            <div>
                <h3>Enter the User name:</h3>
                <form:input type="text" path="userName"/>
            </div>
            <div>
                <h3>Enter the password: </h3>
                <form:input type="password" path="password"/>
            </div>
            <br>
            <input type="submit" value="Login"/>
        </div>
    </form:form>
</center>
</body>
</html>