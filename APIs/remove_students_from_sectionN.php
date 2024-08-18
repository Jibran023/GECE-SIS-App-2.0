<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$sessionID = $_GET['SessionID'];
$courseID = $_GET['CourseID'];
$studentNames = $_GET['StudentNames'];

$result = $con->updateStudentSection2($sessionID, $courseID, $studentNames);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>