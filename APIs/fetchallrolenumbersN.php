<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$cohorts =  $_GET['cohorts'];
$students = $_GET['students'];

$result = $con->getRollNumbersByCohortsAndStudents($cohorts, $students);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
