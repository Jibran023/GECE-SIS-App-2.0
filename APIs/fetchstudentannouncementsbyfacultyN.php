<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$rollNumber = $_GET['id'];

$result = $con->getAnnouncementsForStudent($rollNumber);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
