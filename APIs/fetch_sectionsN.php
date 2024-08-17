<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyID = isset($_GET['facultyID']) ? $_GET['facultyID'] : '';
$courseName = isset($_GET['courseName']) ? $_GET['courseName'] : '';
$sessionDescription = isset($_GET['sessionDescription']) ? $_GET['sessionDescription'] : '';

$result = $con->getSectionsByFacultyCourseSession($facultyID, $courseName, $sessionDescription);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
