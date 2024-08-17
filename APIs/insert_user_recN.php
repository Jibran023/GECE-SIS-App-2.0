<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$announcementID = $_GET['announcementID'];
$facultyNames = array_map('trim', explode(',', $_GET['FacultyMembers']));  // Converts comma-separated string to array and trims spaces
$ID = $_GET['ID'];

$result = $con->addAnnouncementRecipients2($announcementID, $facultyNames, $ID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
