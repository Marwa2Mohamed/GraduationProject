<?php

// required heaeder
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
// get database connection
include_once '../config/Databaselogin.php';
// instantiate job object
include_once '../objects/user.php';
$Gmail =null;
$database = new Database();
$db = $database->getConnection();

$gmail=isset($_POST['gmail']); 

// you deleted the database 


$sql="select updated_date from account where Google_acc='".$gmail."';";
$result=mysqli_query($db,$sql) or die("Unable to connect to MySQL");
$response=array();
$num_of_rows=mysqli_num_rows($result);


if( $num_of_rows> 0 ){
 // This means the gmail is found and the updated date will be sent
    while($row = mysqli_fetch_array($result)){
         array_push($response,array("SEND_updated_date"=>$row['updated_date']));
         }
    echo json_encode(array("server_response"=>$response));
}
else if ($num_of_rows==0){

    // This means gmail not found
    array_push($response,array("SEND_updated_date"=>"0-0-0"));
    echo json_encode(array("server_response"=>$response));
}


?>



