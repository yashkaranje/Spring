<?php
require "conn.php";
header("Content-Type: application/json");

$query = "SELECT * FROM phone_book;";
$result = mysqli_query($conn, $query);
$entries = array();

if(mysqli_num_rows($result)>0){
    while($row = mysqli_fetch_assoc($result)){
	    $entries[] = array("name" => $row['name'], "phoneNumber" => $row['phone_number']);
    }
    echo json_encode($entries);
}else{
    $entries[] = array("Error" => "No entry found");
	echo json_encode($entries);
}
?>