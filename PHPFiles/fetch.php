<?php
$conn = new mysqli("127.0.0.1", "root", "", "arduino");

	$qu = "select * from arduinotab where 1";
	$data = $conn->query($qu);
	
	$row = $data->fetch_assoc();
    echo "Val = ".$row['val']."</br>";

    $conn->close();

?>