<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$rollNumber = $_GET['rollNumber'];

$result = $con->getCoursesStudents($rollNumber);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>