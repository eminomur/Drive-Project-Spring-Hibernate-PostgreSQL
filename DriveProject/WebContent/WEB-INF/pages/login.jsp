<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Log in</title>
	
	<!--Bootstrap CSS-->
	<link rel="stylesheet"
		href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
		integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
		crossorigin="anonymous">
		
<style>
body {
	background: url("resources/webpage-background-image.jpg") no-repeat center center fixed;
    background-size: 100% 100%;
	color: white;
}
.box {
    width: 500px;
    margin: 200px 0;
}
.float {
    position: absolute;
    z-index: 2;
}
.form {
    margin-left: 145px;
}
.form-group {
	text-align: middle;
}
</style>
</head>

<body>
	<div class="container">
        <div class="row justify-content-center align-items-center">
            <div class="col-md-6">
                <div class="box">
                    <div class="float">
                        <form class="form" action="login" method="POST">
                            <div class="form-group">
                                <label for="username" class="text-white">Username</label>
								<br>
                                <input type="text" name="username" class="form-control" value="" />
                            </div>
                            <div class="form-group">
                                <label for="password" class="text-white">Password</label>
								<br>
                                <input type="password" name="password" class="form-control">
                            </div>
                            <div class="form-group">
                                <input type="submit" name="submit" class="btn btn-white " value="Log in" />
                            </div>
                        </form>
                        	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
	      						<font color="red">
	        						Your login attempt was not successful due to<br/><br/>
	        						<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
	      						</font>
							</c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>