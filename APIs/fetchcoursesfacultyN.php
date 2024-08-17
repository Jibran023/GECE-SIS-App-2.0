<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyID = $_GET['facultyID'];
$semesterDescription = $_GET['semesterDescription'];

$result = $con->getFacultyCourses($facultyID, $semesterDescription);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>