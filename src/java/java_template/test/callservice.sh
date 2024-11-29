#!/bin/bash

################################
# THIS SCRIPT IS NOT USABLE AS-IS
# MODIFY IT TO POINT TO THE CORRECT BUCKET, FILENAMES, AND FUNCTION
################################


# JSON object to pass to Lambda Function
# json={"\"row\"":50,"\"col\"":10,"\"bucketname\"":\"test.bucket.462562f24.pib\"","\"filename\"":\"test.csv\""}
json='{"bucketname":"test.bucket.462562f24.pib","originalfilename":"Screenshot.png","newfilename":"scaled_Screenshot.png"}'

echo "Invoking Lambda function using AWS CLI (Boto3)"
time output=`aws lambda invoke --invocation-type RequestResponse --cli-binary-format raw-in-base64-out --function-name grayscaleImage --region us-east-1 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`
echo
echo "JSON RESULT:"
echo $output | jq
echo

