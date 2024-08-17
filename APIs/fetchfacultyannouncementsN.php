<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyID = $_GET['id'];

$result = $con->fetchFacultyAnnouncements($facultyID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
