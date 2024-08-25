<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$MsgIDs = $_GET['MsgIDs'];
$ReadTime = $_GET['ReadTime'];

$result = $con->updateReadTime($MsgIDs, $ReadTime);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>