<?php
require_once "lib/nusoap.php";

function getBanks($creditScore, $duration, $loan) {
	return checkBanks( $creditScore, $duration, $loan);
}

function checkBanks( $creditScore, $duration, $loan){
	$bank1 = true;$bank2 = true;$bank3 = true;$bank4 = true;
	if($creditScore<250){
			$bank3 = false;	
			$bank2 = false;	
	}
	if($duration > 50){
		$bank1 = false;
	}
	if($loan < 10){
		$bank2 = false;	
	}
	if($loan > 10000){
		$bank1 = false;	
		$bank2 = false;	
	}
	$banks;
	if($bank1){
		$banks = "1";
	}
	if($bank2){
		$banks = $banks."2";
	}
	if($bank3){
		$banks = $banks."3";
	}
	if($bank4){
		$banks = $banks."4";
	}
	return $banks;
	
}

$server = new soap_server();
$server->register("getBanks");
$server->service($HTTP_RAW_POST_DATA);

?>