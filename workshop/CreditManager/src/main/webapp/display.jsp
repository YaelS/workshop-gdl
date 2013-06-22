<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display</title>
</head>
<body>
	<div style="border: 2px solid rgb(45, 204, 83); width:800px;">
<center>
<font style="font-size:30px;">HISTORY</font>
	<table style="text-align:center; border:5px rgb(53, 91, 187) solid;" border="3">
		<th>RFC</th><th>LOAN AMOUNT</th><th>QUALIFICATION</th><th>EXPIRATION DATE</th><th>STATUS</th>
			<s:iterator value="allLoans">
		        <tr>
		        <td>
 		        	<s:property value="rfc"/>
		        </td>
		        <td>
 		        	<s:property value="amount"/>
		        </td>
		        <td>
 		        	<s:property value="qualification"/>
		        </td>
		        <td>
 		        	<s:property value="expirationDate"/>
		        </td>
		        <td>
 		        	<s:property value="status"/>
		        </td>
		        </tr>
    		</s:iterator>
	</table>
	<a href="<s:url action='index'/>">Back</a>
</center>
</div>
</body>
</html>