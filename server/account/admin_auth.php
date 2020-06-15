<?php

session_start();
if(!isset($_SESSION["Authenticated"]) || $_SESSION["Authenticated"]!=1){  
    $now = time();
            if ($now > $_SESSION["expire"]) {
            session_unset();
            session_destroy();
            //  header("WWW-Authenticate: Basic realm=\"thetutlage\"");
            //header("HTTP\ 1.0 401 Unauthorized");
            //echo 'unauthorized';    
             header("Location:../../../hr/Loginform.html");   
                exit();
        }

}


?>