<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyID = $_GET['FacultyID'];
$courseID = $_GET['CourseID'];
$sessionID = $_GET['SessionID'];
$sectionID = $_GET['SectionID'];

$result = $con->addFacultyCourseSection($facultyID, $courseID, $sessionID, $sectionID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>