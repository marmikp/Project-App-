<?php

$rnumber = $_POST['rno'];
$name = $_POST['name'];

include "conn.php";
$check = "select * from login where rnomber=".$rnumber;
$arrayjson = "";
$count = 1;
$myJSON = "";
$data = $con->query($check);
$num_rows1 = $data->num_rows;
if ($num_rows1 > 0) {
	$query = "select * from profile_info where uname='".$name."'";
	$ans = $con->query($query);
	$status_raw = $ans->num_rows;
	if ($status_raw > 0) {
		while ($row=mysqli_fetch_row($ans))
		{
			error_reporting(E_ERROR | E_PARSE);
			$jsonObj->name = $row[1];
			$jsonObj->email = $row[3];
			$jsonObj->priority = $row[6];
			$jsonObj->phone = $row[4];
			$jsonObj->img = $row[5];
			$myJSON = json_encode($jsonObj);
		}
	}else{
		error_reporting(E_ERROR | E_PARSE);
		$jsonObj->name = "Not Found";
		$jsonObj->email = "Not Found";
		$jsonObj->priority = "Not Found";
		$jsonObj->phone = "Not Found";
		$jsonObj->img = "Not Found";
		$myJSON = json_encode($jsonObj);
	}
	print_r($myJSON);
}
	

mysqli_close($con);	

?>