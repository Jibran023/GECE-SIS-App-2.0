<?php
session_start();
require_once('../Faculty/class/DB_Queries.php');
$con = new DB_Queries();
$errors='';
if (!empty($_POST) && $_SERVER["REQUEST_METHOD"] === 'POST') {
$UserID = $_SESSION['id'];
$CriteriaID=$_POST['CID'];
$RollNumber=$_POST['RollNumber'];
$Marks=$_POST['Marks'];
$Assessment=$_POST['Assessment'];

if(empty($CriteriaID)){
    $errors.='CID is empty';
}
if(empty($RollNumber)){
    $errors.='RollNumber is empty';
}
if($Marks==null || $Marks<0){
    $errors.='Marks have issues, Provide proper value';
}
if ( empty($errors) ) {
    if($CriteriaID=="LSP-1" || $CriteriaID=="PP-1" || $CriteriaID=="OP-1"){
        $Present=$con->CheckIfStudentIsPresentInDC($RollNumber,$CriteriaID);
        if($Present!=0){
            $result=$con->GBStudentDCMarkingUpdate($RollNumber,$CriteriaID,$Marks,$UserID,$Assessment);
        }
        else{
            $result=$con->GBStudentDCMarkingInsert($RollNumber,$CriteriaID,$Marks,$UserID,$Assessment);
        }
    }
    else{
 
      /*@ Save your data in database and return respose */
      $Present=$con->CheckIfStudentIsPresent($RollNumber,$CriteriaID);
        if($Present!=0){
            $result=$con->GBStudentMarkingUpdate($RollNumber,$CriteriaID,$Marks,$UserID);
        }
        else{
            $result=$con->GBStudentMarkingInsert($RollNumber,$CriteriaID,$Marks,$UserID);
        }
}
      http_response_code( 200 );
      echo json_encode( [ 'msg' => 'Your registration has successfully done' ] );
 
 } else {
 
      /*@ Return errors */
 
      http_response_code( 406 ); 
      echo json_encode( [ 'msg' => $errors ] );
   }

}



?>