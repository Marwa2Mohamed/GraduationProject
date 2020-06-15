<?php
// required headers
//require("admin_auth.php");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once '../config/database.php';
 
// instantiate job object
include_once '../objects/account.php';
 
$database = new Database();
$db = $database->getConnection();
 
$account = new account($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"));
 
// set job property values
$account->Acc_id = $data->Acc_id;
$account->Contact_id = $data->Contact_id;
$account->Time = $data->Time; 
$job->post_date = date("Y-m-d",strtotime($data->post_date)); 
// create the job
if($job->create()){
    echo '{';
        echo '"message": " was created."';
    echo '}';
}
 
// if unable to create the job, tell the user
else{
    echo '{';
        echo '"message": "Unable to create Job."';
    echo '}';
}
?>