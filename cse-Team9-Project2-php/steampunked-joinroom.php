<?php
require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

//if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
//    echo '<room status="no" msg="magic" />';
//    exit;
//}

// Process in a function
//getRoomState($_GET['user'], $_GET['pw']);

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

                    $userid = getUser($pdo, $user, $password);

                    $query = "SELECT id FROM steampunkedroom WHERE userid2='-1' AND name='$roomName'";
                    $rows = $pdo->query($query);
                    if ($rows->rowCount() == 0) {
                        echo "<room status=\"no\"msg='room does not exist'>\r\n";
                        exit;
                    }

                    $row = $rows->fetch();
                    $id = $row["id"];

                    $query = "UPDATE steampunkedroom SET userid2='$userid', currentTurn='1' WHERE id='$id'";

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


//    // Read to the start tag
//    while($xml->read()) {
//        if($xml->nodeType == XMLReader::ELEMENT && $xml->name == "room") {
//            // We have the user tag
//            $magic = $xml->getAttribute("magic");
//            if($magic != "NechAtHa6RuzeR8x") {
//                echo '<room status="no" msg="magic" />';
//                exit;
//            }
//            $user = $xml->getAttribute("username");
//            $password = $xml->getAttribute("password");
//            $roomName = $xml->getAttribute("roomname");
//
//
//            $userid = getUser($pdo, $user, $password);
//
//            $query = "UPDATE steampunkedroom SET userid2=$userid WHERE name=$roomName";
//
//            if(!$pdo->query($query)) {
//                echo '<room status="no" msg="insertfail">' . $query . '</room>';
//                exit;
//            }
//
//            echo '<room status="yes" msg="set up"/>';
//            exit;
//        }
//    }
    echo '<room status="no" msg="invalid XML" />';
}

/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function getRoomState($user, $password) {
    // Connect to the database
    $pdo = pdo_connect();

    $userid = getUser($pdo, $user, $password);

    $query = "SELECT userid1, userid2 from steampunkedroom where userid1=$userid or userid2=$userid";

    $rows = $pdo->query($query);
    if($row = $rows->fetch()) {
        // We found the record in the database
        // Check the password
        if($row['userid1'] != null and $row['userid2'] != null) {
            echo '<room status="yes" msg="ready"/>';
            exit;
        }
        echo '<room status="no" msg="not ready yet"/>';
        exit;
    }
    echo '<room status="no" msg="error"/>';
    exit;
}
?>
