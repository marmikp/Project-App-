<?php

session_start();
try{
	$username1 = $_POST['username'];
	$password1 = $_POST['password'];
	$rnomber1 = $_POST['rnomber'];
	include "conn.php";

	$c = 0;
	if ($rnomber1 == 1) { //First time Login
		$query = "select * from login where uname = '".$username1."' and password = '".$password1."'";
		$data = $con->query($query);
		$num_rows1 = $data->num_rows;
		if ($num_rows1 > 0) {
			$c = 1;
		}
	}

	elseif ($password1 == "") { //Second time Login
		$query = "select * from login where uname = '".$username1."' and rnomber = '".$rnomber1."'";
		$data = $con->query($query);
		$num_rows1 = $data->num_rows;
		if ($num_rows1 > 0) {
			$c = 1;
		}
	}

	if ($c == 1) {
		$random_no = mt_rand(1000,9999);
		$query = "update login set rnomber = '".$random_no."'";
		$con->query($query);
		error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
		$jsonObj->uname = $username1;
		$jsonObj->rnomber = $random_no;
		$jsonObj->response = "1";
		$myJSON = json_encode($jsonObj);
		$_SESSION['user'] = $username1;
		print_r($myJSON);


	}

	else{
		print_r("0,Invalid");
	}
}
catch(Exception $e){
	$jsonObj->response = "0";

}

?>