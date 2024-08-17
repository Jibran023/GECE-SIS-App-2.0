<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();


$result = $con->FetchFaculty();

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
