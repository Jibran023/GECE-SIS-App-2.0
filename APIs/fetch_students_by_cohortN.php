<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$cohorts = isset($_GET['cohorts']) ? explode(',', $_GET['cohorts']) : [];

$result = $con->fetchStudentNamesByCohorts($cohorts);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
