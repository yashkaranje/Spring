<?php
require "conn.php";

$phoneNumber = $_POST["phone-number"];

header("Content-Type: application/json");

$sql_query = "DELETE FROM phone_book WHERE phone_number = ?;";
$stmt = $conn->prepare($sql_query);
$stmt->bind_param("s", $phoneNumber);

if($stmt->execute()){
    if($stmt->affected_rows > 0){
        $msg = array("Success" => "Entry deleted successfully");
        echo json_encode($msg);
    } else {
        $msg = array("Error" => "404");
        echo json_encode($msg);
    }
} else {
    $msg = array("Error" => $stmt->error);
    echo json_encode($msg);
}
?>