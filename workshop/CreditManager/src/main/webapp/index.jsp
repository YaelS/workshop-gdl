<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Credit Manager</title>
</head>
<body>
<div style="border: 2px solid rgb(45, 204, 83); width:300px;">
<center>
<font style="font-size:30px;">Credit burean</font>
<s:form action="rfc">
	<s:textfield name="rfcQuery" label="RFC"/></br>
	<s:select list="#{'1':'Close loan','2':'Payment','3':'Get a new loan','4':'Display'}" name="rfcOptions"/>
	<s:submit value="Submit"/>
</s:form>
</center>
</div>
</body>
</html>