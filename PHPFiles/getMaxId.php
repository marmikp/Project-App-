<?php

$rnumber = $_POST['rno'];



 include "conn.php";
$check = "select * from login where rnomber=".$rnumber;

$data = $con->query($check);
$num_rows1 = $data->num_rows;

if ($num_rows1 > 0) {

	$qid = "select MAX(id) from notification";
	$data = $con->query($qid);
	$row=mysqli_fetch_row($data);
	print_r($row[0]);

}

?>