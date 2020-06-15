<?php
// required headers
require("admin_auth.php");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/job.php';
 
// get database connection
$database = new Database();
$db = $database->getConnection();
 
// prepare product object
$job = new Job($db);
 
// get id of product to be edited
$data = json_decode(file_get_contents("php://input"));
 
// set product property values
$job->Call_id = $data->Call_id;
$job->Contact_id = $data->Contact_id;
$job->Acc_id = $data->Acc_id;
$job->Time = $data->Time;
$job->day = $data->day;
$job->week = $data->week; 
// update the product
if($job->update()){
    echo '{';
        echo '"message": "Product was updated."';
    echo '}';
}
 
// if unable to update the product, tell the user
else{
    echo '{';
        echo '"message": "Unable to update product."';
    echo '}';
}
?>