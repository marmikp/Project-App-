<?php


//session_start();

//if(isset($_SESSION['user'])){
	$rnumber = $POST['rno'];
	$id = (int)$_POST['id'];
	$myJSON = "";

	 include "conn.php";
	$check = "select * from login where rnomber=".$rnumber;
	$arrayjson = "";
	$count = 1;
	$data = $con->query($check);
	$num_rows1 = $data->num_rows;
	if ($num_rows1 > 0) {
		$checkNotification = "select * from notification where flag = 0 and id > ".$id." ORDER BY `id` DESC ";
		$notificationStatus = $con->query($checkNotification);
		$status_raw = $notificationStatus->num_rows;
		if ($status_raw > 0) {
			while ($row=mysqli_fetch_row($notificationStatus))
    		{
    			$jsonObj->name = $row[1];
				$jsonObj->email = $row[3];
				$jsonObj->priority = $row[6];
				$jsonObj->phone = $row[4];
				$jsonObj->img = $row[5];
				$myJSON = json_encode($jsonObj);
    		}
		}else{
			$jsonObj->name = "Not Found";
			$jsonObj->email = "Not Found";
			$jsonObj->priority = "Not Found";
			$jsonObj->phone = "Not Found";
			$jsonObj->img = "Not Found";
			$myJSON = json_encode($jsonObj);
		}

    	print_r($arrayjson);
		
		mysqli_close($con);	
	}

//}

?>
