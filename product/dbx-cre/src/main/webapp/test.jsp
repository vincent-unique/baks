<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%
    Map global = new HashMap();
    global.put("name","vincent");
    global.put("version","0.0.1");
      request.setAttribute("global",global );
%>

<html>
<body>
<h2>${global.name}</h2>

<h3>${requestScope[global].name}</h3>
<h3>$</h3>
</body>

</html>