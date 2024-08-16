<?php
require_once('class/querieshandler.php');

$con = new querieshandler();
$rollNumber = $_GET['rollNumber'];

$result = $con->getCoursesStudent($rollNumber);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>