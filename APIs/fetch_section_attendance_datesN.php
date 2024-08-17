<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyID = isset($_GET['FacultyID']) ? $_GET['FacultyID'] : null;
$sectionID = isset($_GET['SectionID']) ? $_GET['SectionID'] : null;

$result = $con->getAttendanceDatesByFacultyAndSection($facultyID, $sectionID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
