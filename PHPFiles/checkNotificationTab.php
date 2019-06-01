<?php

$rnumber = $_POST['rno'];
include "conn.php";
if (isset($_POST['id'])){
	$id = (int)$_POST['id'];

	$check = "select * from login where rnomber=".$rnumber;

	$data = $con->query($check);
	$num_rows1 = $data->num_rows;
	$arrayjson = "";
	$myjson = "";
	$count = 1;
	$lastid = $id + 10;
	if ($num_rows1 > 0) {
		$checkNotification = "select * from notification where flag = 0 and id > ".$id." ORDER BY `id` DESC limit 10";
		
		$notificationStatus = $con->query($checkNotification);
		$status_raw = $notificationStatus->num_rows;
		
		if ($status_raw > 0) {
			while ($row=mysqli_fetch_row($notificationStatus))
    		{
    			// $arrayjson = $arrayjson."".$row[0].",".$row[1].",".$row[2].",".$row[3].",".$row[4]."";
    			error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
    			$arrayjson[$count]->id = $row[0];
    			$arrayjson[$count]->msg = $row[1];
    			$arrayjson[$count]->date = $row[2];
    			$arrayjson[$count]->time = $row[3];
    			$arrayjson[$count]->img_name = $row[4];
    			// if ($count == $status_raw) {
    			// 	$arrayjson = $arrayjson."";
    			// }
    			// else{
    			// 	$arrayjson = $arrayjson."/";	
    			// }

    			$count = $count + 1;

    		}
    		$myjson = json_encode($arrayjson);
    		print_r($myjson);
		}
		else{
			print_r("");
		}

		
	}
	else{
		print_r("");
	}

}
if (isset($_POST['lastid'])){
	
	$lastid = (int)$_POST['lastid'];

	

	$check = "select * from login where rnomber=".$rnumber;

	$data = $con->query($check);
	$num_rows1 = $data->num_rows;
	$arrayjson = "";
	$myjson = "";
	$count = 1;
	if ($num_rows1 > 0) {
		$checkNotification = "select * from notification where flag = 0 and id < ".$lastid." ORDER BY `id` DESC limit 10";
		
		$notificationStatus = $con->query($checkNotification);
		$status_raw = $notificationStatus->num_rows;
		
		if ($status_raw > 0) {
			while ($row=mysqli_fetch_row($notificationStatus))
    		{
    			// $arrayjson = $arrayjson."".$row[0].",".$row[1].",".$row[2].",".$row[3].",".$row[4]."";
    			error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
    			$arrayjson[$count]->id = $row[0];
    			$arrayjson[$count]->msg = $row[1];
    			$arrayjson[$count]->date = $row[2];
    			$arrayjson[$count]->time = $row[3];
    			$arrayjson[$count]->img_name = $row[4];
    			// if ($count == $status_raw) {
    			// 	$arrayjson = $arrayjson."";
    			// }
    			// else{
    			// 	$arrayjson = $arrayjson."/";	
    			// }

    			$count = $count + 1;

    		}
    		$myjson = json_encode($arrayjson);
    		print_r($myjson);
		}
		else{
			print_r("");
		}

		
	}
	else{
		print_r("");
	}


}




 
	
mysqli_close($con);
?>