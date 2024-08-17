<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$userID = $_GET['userID'];
$title = $_GET['Title'];
$content = $_GET['Content'];
$postedDateTime = $_GET['PostedDateTime'];
$facultyNames = array_map('trim', explode(',', $_GET['FacultyMembers']));

$result = $con->submitAnnouncement($userID, $title, $content, $postedDateTime, $facultyNames);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
