<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$announcementID = $_GET['announcementID'];
$facultyNames = array_map('trim', explode(',', $_GET['FacultyMembers']));

$result = $con->insertFacultyRecipients($announcementID, $facultyNames);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
