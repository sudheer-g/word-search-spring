<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<%@ page session="false" %>
<title>Word Search Service</title>
<center><h1>Word Search Service</h1></center>
<body>
<center>
<form:form name="wordSearch" modelAttribute="word" method="POST" action="results">
<div>
<div>
<h3>Enter the Directory path:</h3>
<form:input type="text" path="directory"/>
</div>
<div>
<h3>Enter the pattern to search: </h3>
<form:input type="text" path="word"/>
</div>
<br>
<input type="submit" value="Search"/>
</div>
</form:form>
<a href="logout"><button>Logout</button></a></button>
</center>
</body>
</html>