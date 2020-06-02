<?php
	$code = $_GET["code"];
	$fp = fopen("code.txt", "w");
	fwrite($fp, $code);
	fclose($fp);
?>