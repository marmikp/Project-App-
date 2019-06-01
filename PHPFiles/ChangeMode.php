<?php

$rno = $_POST['rno'];
$status = (string)$_POST['status'];
$mode = "";
if($status == "change"){
	$mode = (int)$_POST['mode'];	
}



include "conn.php";
$check = "select * from login where rnomber=".$rno;

$data = $con->query($check);
$num_rows1 = $data->num_rows;
$query = "";
if ($num_rows1 > 0) {

	if ($status == "get") {

		$query = "select * from mode where id = 1";
		$data1=$con->query($query);
		$status_raw = $data1->num_rows;
		
		if ($status_raw > 0) {
			
    		$row=mysqli_fetch_row($data1);
    		$i = 0;
    		while ($i != 4) {
    			$i++;
    			if ($row[$i] == 1) {
    				break;
    			}
    		}
    		print_r($i);
    		
		}
    	
	}else{
		
		switch ($mode) {
			case 1:
				$query = "update mode set `low`=1,`normal`=0,`high`=0 where id=1";
				break;
			case 2:
				$query = "update mode set `low`=0,`normal`=1,`high`=0 where id=1";
				break;
			case 3:
				$query = "update mode set `low`=0,`normal`=0,`high`=1 where id=1";
				break;
		}

		if ($con->query($query)) {
			print_r("true");
		}
		else{
			print_r("false");
		}

	}

}
else{
	print_r("invalid");
}
mysqli_close($con);

?>