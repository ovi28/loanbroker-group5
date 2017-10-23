<?php
require_once "lib/nusoap.php";

function getInterest( $ssn,$creditScore, $duration, $loan) {
	return join(",", array(
            $ssn,
            calculateLoan( $creditScore, $duration, $loan)));
     
	
}

function calculateLoan( $creditScore, $duration, $loan){
	if ($creditScore > 670) {
            if ($duration > 120) {
                if ($loan > 15) {
                    return 4.6;
                } else {
                    return 6.8;
                }
            } else {
                if ($loan > 15) {
                    return 3.6;
                } else {
                    return 5.8;
                }
            }
        } else {
            if ($duration > 120) {
                if ($loan > 15) {
                    return 13.6;
                } else {
                    return 15.8;
                }
            } else {
                if ($loan > 15) {
                    return 12.6;
                } else {
                    return 14.8;
                }
            }
        }
}

$server = new soap_server();
$server->register("getInterest");
$server->service($HTTP_RAW_POST_DATA);

?>