<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$announcementID = isset($_GET['announcementID']) ? $_GET['announcementID'] : '';
$rollNumbers = isset($_GET['rollNumbers']) ? $_GET['rollNumbers'] : '';
$sectionID = isset($_GET['sectionID']) ? $_GET['sectionID'] : '';

$result = $con->insertAnnouncementRecipients($announcementID, $rollNumbers, $sectionID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
