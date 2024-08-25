<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$FacultyName = $_GET['FacultyName'];

$result = $con->getNotificationsByFacultyName($FacultyName);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>