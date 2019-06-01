<?php

$rnomber=$_POST['rno'];
$id=$_POST['id'];
$status = $_POST['status'];
$name = "";
$imgName = "";
$check = 0;
if ($status == "1") {
	$name = $_POST['name'];
	$imgName = $_POST['imgName'];
	$checktrain = (int)$_POST['train'];
}

 include "conn.php";
	$check = "select * from login where rnomber=".$rnomber;

	$data = $con->query($check);
	$num_rows1 = $data->num_rows;
	$query = "";
	$query1 = "";
	$count = 1;
	if ($num_rows1 > 0) {

		if ($status == 1) {
			$query = "update notification set `flag` = 1, `accept` = 1 where id = ".$id;
			$query1 = "insert into for_asking(`name`,`img_name`,`flag`) values ('".$name."','".$imgName."',".$checktrain.")";

		}
		elseif ($status == 0) {
			$query="update notification set `flag` = 1 where id = ".$id;

		}
		
		if ($con->query($query)) {
			if($status == 1){
				if ($con->query($query1)) {
					print_r("1");
				}
				else{
					print_r("0");
				}
			}else{
				print_r("1");
			}

		}
		else{
			print_r("0");
		}
		
	}
	else{
		print_r("0");
	}

	mysqli_close($con);


?>