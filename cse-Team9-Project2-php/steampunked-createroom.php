<?php
require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

// Ensure the xml post item exists
if(!isset($_POST['xml'])) {
    echo '<room status="no" msg="missing XML" />';
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
        echo '<room status="no" msg="invalid XML" />';
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
                echo '<room status="no" msg="magic" />';
                exit;
            }

            // Read to the hatting tag
            while($xml->read()) {
                if ($xml->nodeType == XMLReader::ELEMENT &&
                    $xml->name == "room") {

                    $user = $xml->getAttribute("username");
                    $password = $xml->getAttribute("password");
                    $roomName = $xml->getAttribute("roomname");
                    $size = $xml->getAttribute("size");

                    $userid = getUser($pdo, $user, $password);

                    // Set nulls to -1
                    $query = "INSERT INTO steampunkedroom(userid1, name, size, userid2, currentTurn) VALUES('$userid', '$roomName', '$size', '-1', '-1')";

                    if (!$pdo->query($query)) {
                        echo '<room status="no" msg="insertfail">' . $query . '</room>';
                        exit;
                    }

                    echo '<room status="yes" msg="set up"/>';
                    exit;

                }
            }

        }
    }
    echo '<room status="no" msg="invalid XML" />';
}
?>