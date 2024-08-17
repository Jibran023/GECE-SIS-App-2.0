<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$userID = $_GET['id'];

$result = $con->getAnnouncementsForUser($userID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
