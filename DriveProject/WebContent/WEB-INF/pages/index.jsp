<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hello ${userName}</title>
</head>
<body>
	<h3>File Upload</h3>
	<form action="upload" method="post"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td>Select File</td>
				<td><input type="file" name="file"></td>
				<td><button type="submit">Upload</button></td>
			</tr>
		</table>
	</form>
	<br />
	<hr />
	<span style="color: red; font-size: 14px;">${msg}</span>

</body>
</html>