<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyName = $_GET['FacultyName'];

$result = $con->getStudentNotifications($facultyName);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>