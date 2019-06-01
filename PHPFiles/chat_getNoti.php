<?php


include "chat_con.php";
$query = "select * from notification";
$data = $con->query($query);
$num_rows1 = $data->num_rows;

if ($num_rows1 > 0) {
	
	
	while ($row=mysqli_fetch_assoc($result))
	{
		$jsonObj->id = $row['id'];
		$jsonObj->name = $row['msg'];
		$myJSON = json_encode($jsonObj);
	}
	
	print_r($arrayjson);
	
	mysqli_close($con);	
}


?>