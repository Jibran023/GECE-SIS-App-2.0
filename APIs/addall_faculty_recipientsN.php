<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$announcementID = $_GET['announcementID'];

$result = $con->addFacultyRecipients($announcementID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
