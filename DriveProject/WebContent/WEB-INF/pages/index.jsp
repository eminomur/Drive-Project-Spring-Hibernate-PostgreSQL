<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Index</title>
	
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
<style>
	body {
		background: url("resources/webpage-background-image.jpg") no-repeat center center fixed;
	    background-size: 100% 100%;
		color: white;
	}
	
	.navbar-inverse {
		background: rgba(0,0,0,0);
	}
	
	.table-bordered {
		border-radius: 5px;
	    width: 50%;
	    margin: 0px auto;
	    float: none;
	    text-align: center;
	}
	
	.upload {
	   margin-left:auto;
	   margin-right:auto;
	   width:300px;
	}
	
	.search {
		position: relative;
		left: 315px;
	}
	
	.search-input {
		background-color: black;
	}
	
	.search-button {
		background-color: white;
		color: black;
	}
	
	.select-go {
		background-color: black;
		position: relative;
		left: 315px;
	}
	
	.select-go-button {
		background-color: white;
		color: black;
		position: relative;
		left: 25%;
	}
</style>
</head>
<body>
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="/DriveProject">DRIVE</a>
			</div>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="downloadlist/${user}">Download File List</a></li>
				<li><a href="logout"><span class="glyphicon glyphicon-log-out"></span>Logout ${user}</a></li>
			</ul>
		</div>
	</nav> 
	<br>
	<h3><p style="text-align:center" >File Upload</p></h3>
	<br>
  	<form class="upload" action="upload" method="POST" enctype="multipart/form-data">
    	<div class="form-group">
      		<input type="file" name="file" multiple="multiple">
    	</div>
    	<div class="form-group">
      		<label for="topic">Type of Program</label>
      		<input type="text" class="form-control" name="topic">
    	</div>
    	<div class="form-group">
      		<label for="keywords">Keywords</label>
      		<input type="text" class="form-control" name="keywords">
    	</div>
    	<button type="submit" class="btn btn-default">Upload</button>
  	</form>
	<br>
	<span style="color: ${color}; font-size: 14px;"><p style="text-align:center;" >${msg}</p></span>
	<br>
	<form class="search" action="search">
		<table>
			<tr>
				<td><input class="search-input" type="text" name="keywords" value="${keywords}"></td>
				<td><input class="search-button" type="submit" value="Search"></td>
			</tr>
		</table>
	</form>
	<table class="table table-bordered" style="float: center">
		<thead>
			<tr>
				<th>File Name</th>
				<th>Upload Date</th>
				<th>Topic</th>
				<th>Keywords</th>
			</tr>
		</thead>
		<tbody>
				<c:forEach items="${userFileList}" var="item">
					<tr>
						<td><c:out value="${item.fileName}" /></td>
						<td><c:out value="${item.uploadDate}" /></td>
						<td><c:out value="${item.topic}"></c:out></td>
						<td><c:out value="${item.keywords}"></c:out></td>
						<td><a href="download/${item.fileId}">Download</a></td>
						<td><a href="delete/${item.fileId}">Delete</a></td>
					</tr>
				</c:forEach>
		</tbody>
	</table>
	<form action="go">
		<select class="select-go" name="pagenum">
			<c:forEach items="${size}" var="page">
				<option value="${page}">${page}</option>
			</c:forEach>
		</select>
		<input class="select-go-button" type="submit" value="Go">
	</form>
</body>
</html>