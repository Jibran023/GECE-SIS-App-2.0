<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$announcementID = $_GET['announcementID'];
$rollNumbers = array_map('intval', explode(',', $_GET['rollNumbers']));

$result = $con->insertAnnouncementRecipients2($announcementID, $rollNumbers);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
