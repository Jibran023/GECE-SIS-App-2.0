<?php
require_once('class/adminquerieshandler.php');
$con=new querieshandler();
$result=$con->FetchStudentsData();
print_r($result);
?>
