<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$courseName = trim($_GET['CourseName']);
$sessionDescription = trim($_GET['SessionDescription']);

$result = $con->getRollNumbersForCourseAndSession($courseName, $sessionDescription);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
