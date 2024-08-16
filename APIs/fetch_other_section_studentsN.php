<?php
require_once('class/querieshandler.php');

$con = new querieshandler();
$year = $_GET['year'];
$rollNumber = $_GET['rollNumber'];

$result = $con->getStudentMapData($year, $rollNumber);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>