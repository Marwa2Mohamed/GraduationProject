
<?php
// include database and object files
include_once '../config/Databaselogin.php';
include_once '../objects/user.php';
session_start();
// get database connection
$database = new Database();
$db = $database->getConnection();
// prepare user object
$user = new User($db);
// set ID property of user to be edited
$user->username = isset($_POST['username']) ? $_POST['username'] : die();
$user->password = (isset($_POST['password']) ? $_POST['password'] : die());
// read the details of user to be edited
$stmt = $user->login();


if($stmt->rowCount() > 0){
// get retrieved row
$row = $stmt->fetch(PDO::FETCH_ASSOC);
// create array
   
$user_arr=array(
"status" => true,
"message" => "Successfully Login!",
"username" => $row['usern']
);  
while($row = mysqli_fetch_array($result)){         array_push($response,array("SEND_updated_date"=>$row['updated_date']));}
echo json_encode(array("server_response"=>$response)); 
    $_SESSION["username"]=$user->username;
    $_SESSION["password"]=$user->password;
    $_SESSION["Authenticated"]=1;
    
   // $_SESSION["start"] = time(); // Taking now logged in time.
    // Ending a session in 30 minutes from the starting time.
    $_SESSION["expire"] =time() + (30*60);
    
    
header("Location:../../../hr/hr_updating.php");
}

elseif(isset($_GET["logout"])){
    session_destroy();
    echo"here";
header("Location:http://localhost/hr/Loginform.html"); 
    
}


else{
  $_SESSION["Authenticated"]=0;  
 header("Location:../../../hr/Loginform.html");   
$message = "your username or password invalid";
echo "<script type='text/javascript'>alert('$message');</script>";
//header("Location:http://localhost/hr/Loginform.html"); 
array_push($response,array("SEND_updated_date"=>"0-0-0"));
echo json_encode(array("server_response"=>$response));   
session_write_close();
}


// make it json format
print_r(json_encode($user_arr));
?>








