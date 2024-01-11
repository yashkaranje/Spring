<?php
require "conn.php";

$name = $_POST["name"];
$phoneNumber = $_POST["phone-number"];

header("Content-Type: application/json");

$sql_query = "INSERT INTO phone_book (name, phone_number) VALUES (?, ?);";
$stmt = $conn->prepare($sql_query);
$stmt->bind_param("ss", $name, $phoneNumber);

if($stmt->execute()){
    $msg = array("Success" => "Entry added successfully");
    echo json_encode($msg);
}else{
    $error = array("Error" => $stmt->error);
    echo json_encode($error);
}
?>