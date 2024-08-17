<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$announcementID = intval($_GET['announcementID']);
$rollNumbers = $_GET['rollNumbers'];

$result = $con->addAnnouncementRecipients($announcementID, $rollNumbers);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
