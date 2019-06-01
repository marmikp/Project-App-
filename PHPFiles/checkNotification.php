<?php

//session_start();

//if(isset($_SESSION['user'])){
	$rnumber = $_POST['rno'];
	$date = $_POST['date'];
	$time = $_POST['time'];
	$status = $_POST['status'];
	$id = (int)$_POST['id'];


	 include "conn.php";
	$check = "select * from login where rnomber=".$rnumber;

	$data = $con->query($check);
	$num_rows1 = $data->num_rows;
	if ($num_rows1 > 0) {
		$checkNotification = "select * from notification where flag = 0 and id > ".$id." ORDER BY `id` DESC";
		if ($status == 0) {
			print_r("0");
		}else{
			$notificationStatus = $con->query($checkNotification);
			$status_raw = $notificationStatus->num_rows;
			$row=mysqli_fetch_row($notificationStatus);
			$date = date('d-m-Y', strtotime($date));
			$time = date('h-i-sa', strtotime($time));
			$date1 = date('d-m-Y', strtotime($row[2]));
			$time1 = date('h-i-sa', strtotime($row[3]));

			if ($status_raw > 0) {
				if ($date <= $date1) {
					if ($date == $date1) {
						if ($time < $time1){
							print_("1");
						}
						else{
							print_r("");
						}
					}else{
						print_r("1");
					}
				}
				else{
					print_r("");
				}
			}
			else{
				print_r("");
			}
		}
	}
	mysqli_close($con);
//}

?>