<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$userID = $_GET['userID'];
$title = $_GET['Title'];
$content = $_GET['Content'];
$postedDateTime = $_GET['PostedDateTime'];
$sentTo = $_GET['SentTo'];
$receiverID = $_GET['ReceiverID'];

$result = $con->insertAnnouncement5($userID, $title, $content, $postedDateTime, $sentTo, $receiverID);

header('Content-Type: application/json');

?>
