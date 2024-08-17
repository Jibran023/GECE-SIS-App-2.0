<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$courseName = isset($_GET['CourseName']) ? $_GET['CourseName'] : '';
$sessionDescription = isset($_GET['SessionDescription']) ? $_GET['SessionDescription'] : '';
$sectionName = isset($_GET['SectionName']) ? $_GET['SectionName'] : '';

$result = $con->getRollNumbersAndSectionID($courseName, $sessionDescription, $sectionName);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
