<?php
// required headers
//require("admin_auth.php");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/account.php';
 
// instantiate database and job object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$job = new account($db);
 
// query jobs
$stmt = $job->read();

$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){
 
    // products array
    $jobs_arr=array();
    $jobs_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $jobs_item=array(
            "Acc_id" => $Acc_id,
            "Google_acc" => $Google_acc,
            "last_updated" => $last_updated
        );
        //echo json_encode($product_item);
        array_push($jobs_arr["records"], $jobs_item);
    }
 
    echo json_encode($jobs_arr);
}
 
else{
    echo json_encode(
        array("message" => "No jobs found.")
    );
}
?>