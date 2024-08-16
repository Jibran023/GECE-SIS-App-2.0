<?php
require_once('class/querieshandler.php');

$con = new querieshandler();
$sessionID = $_GET['SessionID'];
$courseID = $_GET['CourseID'];
$sectionID = $_GET['SectionID'];
$studentNames = $_GET['StudentNames'];

$result = $con->updateStudentSection($sessionID, $courseID, $sectionID, $studentNames);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>