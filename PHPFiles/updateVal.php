<?php

$var = $_GET['val'];
$qu = "update arduinoTab set `val` = $var where id = 1";
$conn = new mysqli("127.0.0.1", "root", "", "arduino");

$conn -> query($qu);
$conn->close();
?>