<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$userID = $_GET['userID'];
$title = $_GET['Title'];
$content = $_GET['Content'];
$postedDateTime = $_GET['PostedDateTime'];

$result = $con->insertAnnouncement($userID, $title, $content, $postedDateTime);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
