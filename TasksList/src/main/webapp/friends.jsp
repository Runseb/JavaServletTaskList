<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Friends</title>
</head>
<body>
<p>${friends}</p>
<p>${info}</p>
<p>${error}</p>
<form action="friends" method="POST">
    Input friend's email: <input name="friendMail" />
    <input type="submit" value="Add friend" />
    <br>
</form>
<p><a href="tasks">Go to Tasks</a></p>
<p><a href="targets">Go to Targets</a></p>
</body>
</html>