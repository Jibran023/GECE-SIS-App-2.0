<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$attendanceStatus = $_GET['AttendanceStatus'];
$name = $_GET['Name'];
$date = $_GET['Date'];
$sessionID = $_GET['SessionID'];
$courseID = $_GET['CourseID'];
$sectionID = $_GET['SectionID'];

$result = $con->markAttendance($attendanceStatus, $name, $date, $sessionID, $courseID, $sectionID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
