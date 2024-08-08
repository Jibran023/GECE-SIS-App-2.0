<?php

// Function to encrypt data using AES and then encode to Base64
function encryptData($data, $secretKey) {
    $method = 'aes-256-cbc'; // Encryption method
    $key = str_pad($secretKey, 32, '0'); // Ensure key length is 32 bytes (256 bits)
    $iv = '1234567890123456'; // Fixed IV (16 bytes)

    // Encrypt the data
    $encryptedData = openssl_encrypt($data, $method, $key, 0, $iv);

    // Combine IV and encrypted data
    $encryptedDataWithIv = base64_encode($iv . $encryptedData);

    return $encryptedDataWithIv;
}

// Database connection parameters
$host = '127.0.0.1';
$username = 'root';
$password = '';
$database = 'gecesisapp';

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// SQL query to select all columns from the users table
$sql = "SELECT id, Email, Password, department FROM users";
$result = $conn->query($sql);

// Create an array to hold the results
$data = array();

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
} else {
    $data = array("message" => "No results");
}

// Convert the data array to JSON
$jsonData = json_encode($data);

// Secret key for encryption
$secretKey = '5v5CzfuycMTBnEu7X2KwbZ8qg65O9RPZr43gXTL5zMDobBa0qgJxUhbACYIfTGAX'; // Key

// Encrypt the JSON data
$encryptedData = encryptData($jsonData, $secretKey);

// Set the content type to JSON
header('Content-Type: application/json');

// Output the encrypted Base64 encoded data
echo $encryptedData;

// Close connection
$conn->close();
?>
