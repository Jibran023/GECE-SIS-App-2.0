<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$courseID = $_GET['CourseID'];
$sectionName = $_GET['SectionName'];

$result = $con->insertSection($courseID, $sectionName);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>