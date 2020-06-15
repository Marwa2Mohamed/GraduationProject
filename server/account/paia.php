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

$user = new User($db);
$Gmail = isset($_POST["gmail"]) ? $_POST["gmail"] : die();
// $Gmail = "yasso@hotmail.com";

$user->Google_acc = $Gmail ;
$stmt = $user->login() ;
$num = $stmt->rowCount() ;

if($num>0){
 // This means the gmail is found and the updated date will be sent
    while($row = $stmt->fetch(PDO::FETCH_ASSOC)){     // array_push($response,array("SEND_updated_date"=>$row['last_updated']));
        extract($row);
        //global $jobs_arr;
        $jobs_item=array("Acc_id" => $Acc_id,"last_updated" => $last_updated);
        echo json_encode($jobs_item);
        //array_push($jobs_arr, $jobs_item);   
        $jobs_arr[]=$jobs_item;      
    }
    //echo json_encode($jobs_arr);
    //echo json_encode(array("server_response"=>$response["records"]));
}
else if ($num==0){
        //global $jobs_arr;
 
        //array_push($jobs_arr["records"], $jobs_item);
      //$response=array();
      //This means gmail not found
      //array_push($response,array("SEND_updated_date"=>"0-0-0"));
      //echo json_encode(array("server_response"=>$response));
        $today = date("Y-m-d");  
        $user->last_updated = date("Y-m-d",strtotime($today));
        $stm =$user->createAcc();
        $num = $stm->rowCount() ;

       if($num>0){
          
         while($row = $stm->fetch(PDO::FETCH_ASSOC)){     // array_push($response,array("SEND_updated_date"=>$row['last_updated']));
        extract($row);
        //global $jobs_arr;
        $jobs_item=array("Acc_id" => $Acc_id,"last_updated" => "0-0-0");
        echo json_encode($jobs_item);
        //array_push($jobs_arr, $jobs_item);   
        $jobs_arr[]=$jobs_item;      
    }
     
     
        //$jobs_item=array("Acc_id" =>" " ,"last_updated" => "0-0-0");
        //echo json_encode($jobs_item);
        // $jobs_arr[]=$jobs_item; 
           // $jobs_item=array("Acc_id" =>" " ,"last_updated" => "0-0-0");
           // echo json_encode($jobs_item);
           // array_push($jobs_arr, $jobs_item);         

           echo '{';
           echo '"message": "Job was created."';
           echo '}';
       } else{
          
           echo '{';
           echo '"message": "Unable to create Job."';
           echo '}';
       }
    
}




?>



