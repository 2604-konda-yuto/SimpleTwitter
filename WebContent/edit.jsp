<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>ログイン</title>
 		<link href="./css/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div class="main-contents">

		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="errorMessage">
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>

		<div class="header">
			<c:if test="${ not empty loginUser }">
				<a href="./">ホーム</a>
				<a href="setting">設定</a>
				<a href="logout">ログアウト</a>
			</c:if>
		</div>

		<div class="form-area">
        	<div class="message">
				<form action="edit" method="post">
					つぶやき<br />
					<textarea name="editText" cols="100" rows="5" class="tweet-box">${message.text}</textarea>
					<input type="hidden" name="editId" value="${message.id}">
					<br />
					<input type="submit" value="更新">
				</form>
			</div>
		</div>
			<a href="./">戻る</a>
			<div class="copyright">近田　悠人</div>
		</div>

	</body>
</html>