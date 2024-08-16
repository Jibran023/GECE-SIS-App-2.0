<?php
require_once('class/querieshandler.php');

$con = new querieshandler();
$year = $_GET['year'];
$rollNumber = $_GET['rollNumber'];
$id = $_GET['id'];

$result = $con->getStudentData($year, $rollNumber, $id);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>