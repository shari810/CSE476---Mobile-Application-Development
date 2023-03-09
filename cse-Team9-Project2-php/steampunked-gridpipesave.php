<?php
require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

// Ensure the xml post item exists
if(!isset($_POST['xml'])) {
    echo '<user status="no" msg="missing XML" />';
    exit;
}

processXml(stripslashes($_POST['xml']));

echo '<user status="yes"/>';

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
            // We have the hatter tag
            $magic = $xml->getAttribute("magic");
            if($magic != "NechAtHa6RuzeR8x") {
                echo '<user status="no" msg="magic" />';
                exit;
            }

            // Read to the hatting tag
            while($xml->read()) {
                if($xml->nodeType == XMLReader::ELEMENT &&
                    $xml->name == "steampunkedgridpipe") {

                    $connect0 = $xml->getAttribute("connect0");
                    $connect1 = $xml->getAttribute("connect1");
                    $connect2 = $xml->getAttribute("connect2");
                    $connect3 = $xml->getAttribute("connect3");
                    $originconnect0 = $xml->getAttribute("originconnect0");
                    $originconnect1 = $xml->getAttribute("originconnect1");
                    $originconnect2 = $xml->getAttribute("originconnect2");
                    $originconnect3 = $xml->getAttribute("originconnect3");
                    $x = $xml->getAttribute("x");
                    $y = $xml->getAttribute("y");
                    $id = $xml->getAttribute("id");
                    $user = $xml->getAttribute("user");
                    $pw = $xml->getAttribute("pw");
                    $rotation = $xml->getAttribute("rotation");

                    $roomid = getRoom($pdo, $user, $pw);
//                    $query = "SELECT currentTurn from steampunkedroom where id='$roomid'";
//
//                    $rows = $pdo->query($query);
//                    if($row = $rows->fetch()) {
//                        // We found the record in the database
//                        // Check the password
//                        $currentTurn = $row['currentTurn'];
//                    }
//
//                    if ($currentTurn == "1")
//                        $nextTurn = "2";
//                    else
//                        $nextTurn = "1";
//                    $query = "UPDATE steampunkedroom SET currentTurn='$nextTurn' WHERE id='$roomid'";

//                    if (!$pdo->query($query)) {
//                        echo '<room status="no" msg="insertfail">' . $query . '</room>';
//                        exit;
//                    }

                    // Checks
                    if(!is_numeric($x) || !is_numeric($y) || !is_numeric($id)) {
                        echo '<user status="no" msg="invalidnums" />';
                        exit;
                    }

                    $query = <<<QUERY
INSERT INTO steampunkedgridpipes(connect0, connect1, connect2, connect3, originconnect0, originconnect1, originconnect2, originconnect3, x, y, id, player, rotation)
VALUES($connect0, $connect1, $connect2, $connect3, $originconnect0, $originconnect1, $originconnect2, $originconnect3, $x, $y, $id, $roomid, $rotation)
QUERY;

                    if(!$pdo->query($query)) {
                        echo '<user status="no" msg="insertfail">' . $query . '</user>';
                        exit;
                    }

                    echo '<user status="yes"/>';
                    exit;
                }
            }
        }
    }

    echo '<user save="no" msg="invalid XML" />';
}