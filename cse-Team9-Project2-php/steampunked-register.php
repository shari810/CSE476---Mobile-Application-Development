<?php
require_once "db.inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

// Ensure the xml post item exists
if(!isset($_POST['xml'])) {
    echo '<user status="no" msg="missing XML" />';
    exit;
}


processXml(stripslashes($_POST['xml']));

/**
 * Process the XML query
 * @param $xmltext the provided XML
 */
function processXml($xmltext) {
    // Load the XML
    $xml = new XMLReader();
    if(!$xml->XML($xmltext)) {
        echo '<user status="no" msg="invalid XML" />';
        exit;
    }

    // Connect to the database
    $pdo = pdo_connect();
    // Read to the start tag
    while($xml->read()) {
        if($xml->nodeType == XMLReader::ELEMENT && $xml->name == "user") {
            // We have the user tag
            $magic = $xml->getAttribute("magic");
            if($magic != "NechAtHa6RuzeR8x") {
                echo '<user status="no" msg="magic" />';
                exit;
            }
            $user = $xml->getAttribute("username");
            $password = $xml->getAttribute("password");

            $query = "REPLACE INTO steampunkeduser(user, password) VALUES('$user', '$password')";

            if(!$pdo->query($query)) {
                echo '<user status="no" msg="insertfail">' . $query . '</user>';
                exit;
            }

            echo '<user status="yes"/>';
            exit;
        }
    }
    echo '<user status="no" msg="invalid XML" />';
}
?>


