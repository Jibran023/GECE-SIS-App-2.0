<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$studentname = $_GET['studentname'];
$semesterDescription = $_GET['semesterDescription'];

$result = $con->getCoursesByStudentNameAndSemester($studentname, $semesterDescription);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
