<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Targets</title>
</head>
<body>
<p>${targets}</p>
<p>${info}</p>
<p>${error}</p>
<form action="targets" method="POST">
Description: <input name="description" />
<input type="submit" value="Add new Target" />
</form>
<p><a href="tasks">Go to Tasks</a></p>
<p><a href="friends">Go to Friends</a></p>
</body>
</html>