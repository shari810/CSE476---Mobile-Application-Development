<?php
$title = "CSE476 Step 6 Test Page";
//
//  The user and password below are those you used in your
//  android application.
//
$user = "sharinic";  // verify that your userid is correct
$password = "mypAssw0rd"; // change this to use your application password

$base_url = "https://webdev.cse.msu.edu/~sharinic/cse476/step6/"; // verify that this is the correct path to your web site
$magic = "NechAtHa6RuzeR8x";
?>
<html>
<head>
    <title><?php echo $title; ?></title>
</head>
<body>
<h1><?php echo $title; ?></h1>
<h2>Hatter Save Testing</h2>
<p>Paste the valid xml created to save a hatting in the input box below,
    then click the "Test Save" Button. On the new page that appears use the
    browser "View page source" option to see the results.</p>
<form method="post" target="_blank" action="<?php echo $base_url; ?>hatter-save.php">
    <p>XML: <textarea name="xml"></textarea></p>
    <p><input type="submit" value="Test Save" /></p>
</form>

</body>
</html>

<hr />
<h2>Hatter Catalog Testing</h2>
<form method="get" target="_blank" action="<?php echo $base_url; ?>hatter-cat.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <input type="hidden" name="user" value="<?php echo $user; ?>" />
    <input type="hidden" name="pw" value="<?php echo $password; ?>" />
    <input type="submit" value="Test Catalog" />
</form>

<hr />
<h2>Hatter Load Testing</h2>
<p>Enter the id for a hatting in the input box below then click the "Test Load" button to test.</p>
<form method="get" target="_blank" action="<?php echo $base_url; ?>hatter-load.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <input type="hidden" name="user" value="<?php echo $user; ?>" />
    <input type="hidden" name="pw" value="<?php echo $password; ?>" />
    <p>Hatter ID: <input type="text" name="id" value="" /></p>
    <p> <input type="submit" value="Test Load" /></p>
</form>