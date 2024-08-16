<?php
require_once('class/adminquerieshandler.php');
$con=new querieshandler();
$result=$con->FetchFacultyData();
print_r($result);
?>