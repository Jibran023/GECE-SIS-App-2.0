<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyName = $_GET['FacultyName'];
$userID = $_GET['USER'];

$result = $con->fetchNotifications($facultyName, $userID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>