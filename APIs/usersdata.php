<?php
require_once('class/adminquerieshandler.php');
$con=new querieshandler();
$result=$con->LoginQuery2();
print_r($result);
?>
