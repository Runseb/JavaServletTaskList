<%@ page isELIgnored ="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Targets</title>
</head>
<body>
<p>${tasks}</p>
<p>${info}</p>
<p>${error}</p>
<form action="tasks" method="POST">
    Description: <input name="description" />
    <br><br>
    Text: <input name="text" />
    <br><br>
    Deadline: <input type= "date" name="deadline" />
    <br><br>
    State: <input type="radio" name="state" value="ready" checked />Ready
    <input type="radio" name="state" value="not ready" />Not ready
    <br><br>
    <input type="submit" value="Add new Task" />
</form>
<p><a href="targets">Go to Targets</a></p>
<p><a href="friends">Go to Friends</a></p>
</body>
</html>