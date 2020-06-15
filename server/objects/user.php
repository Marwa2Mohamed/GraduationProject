<?php
class User{
// database connection and table name
    private $conn;
    private $table_name = "account";
    // object properties
    public $Google_acc;
    public $Acc_id;
    public $last_updated;
    
        // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function login(){
    // select all query
    $query = "SELECT last_updated , Acc_id
    FROM account
    WHERE
    Google_acc=:Google_acc";
    // prepare query statement

    
    $stmt = $this->conn->prepare($query);
    // sanitize
    $Google_acc=htmlspecialchars(strip_tags($this->Google_acc));   
    $stmt->bindParam(":Google_acc", $this->Google_acc);
           
    // execute query
    $stmt->execute();
    
return $stmt;   
}

    
    function createAcc(){
        
        // select all query
        $query = "INSERT INTO account(Google_acc,last_updated) Values (:Google_acc,:last_updated);";//Google_acc =:Google_acc , last_updated =:last_updated;
        // prepare query statement
        $stmt = $this->conn->prepare($query);
        // saitize
        $this->Acc_id=htmlspecialchars(strip_tags($this->Acc_id));
        $this->Google_acc=htmlspecialchars(strip_tags($this->Google_acc));
        $this->last_updated = $this->last_updated;    
        //bind value
        //$stmt->bindParam(":Acc_id", $this->Acc_id);
        $stmt->bindParam(":Google_acc", $this->Google_acc); 
        $stmt->bindParam(":last_updated", $this->last_updated);        
        // execute query
        if($stmt->execute()){
           // echo "hello";
            $query = "SELECT  Acc_id
            FROM account
            WHERE
            Google_acc=:Google_acc";
            $stmt = $this->conn->prepare($query);
            // sanitize
            $Google_acc=htmlspecialchars(strip_tags($this->Google_acc));   
            $stmt->bindParam(":Google_acc", $this->Google_acc);
                
            // execute query
            $stmt->execute();
            
        return $stmt;   


        }
        
        else{
             return false;
        }
//    return $stmt;
        }    
    
}

?>