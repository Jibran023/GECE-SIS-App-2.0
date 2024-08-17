<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$rollNumber = $_GET['rollNumber'];
$semesterDescription = $_GET['semesterDescription'];

$result = $con->getCoursesStudent($rollNumber, $semesterDescription);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>