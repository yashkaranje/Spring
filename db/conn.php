<?php
require "config.php";

$dbname = DB_NAME;
$user = USER;
$password = PWD;
$host = DB_HOST;
$conn = new mysqli($host, $user, $password, $dbname);

/*
if($conn){
	echo "Connection Success\n";
}else{
	echo "Connection Failed\n";
}
*/
?>