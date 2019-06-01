<?php

$rnomber=$_POST['rno'];
$id=$_POST['id'];


 include "conn.php";
	$check = "select * from login where rnomber=".$rnomber;

	$data = $con->query($check);
	$num_rows1 = $data->num_rows;
	$img_name = "";
	$count = 1;
	if ($num_rows1 > 0) {

		$query="Select `img_name` from notification where flag=0 and id=".$id;
		

		$data1=$con->query($query);
		$status_raw = $data1->num_rows;
		
		if ($status_raw > 0) {
			
    		$row=mysqli_fetch_row($data1);
    		$img_name = $row[0];
    		

    	}

	}

	print_r($img_name);
	mysqli_close($con);

?>