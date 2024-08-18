<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$id = $_GET['id'];

$result = $con->getFacultyById($id);

// Output the result as JSON
header('Content-Type: application/json');
// echo json_encode($result);
?>