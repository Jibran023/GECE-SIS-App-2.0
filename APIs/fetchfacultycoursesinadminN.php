<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$FacultyName = $_GET['FacultyName'];
$semesterDescription = $_GET['semesterDescription'];

$result = $con->getCoursesByFacultyAndSemester($FacultyName, $semesterDescription);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
