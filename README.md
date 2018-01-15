# About
This application is a hack for the Flipkart Affiliate Program https://affiliate.flipkart.com/, that generates particular amount of Flipkart android app Installation, for the Affiliate's tracking id.

# How it works
It is completely based on the Flipkart APIs (Intercepted). There are certain parameters (like Android device id) that helps flipkart to check whether the app was installed first time. So I intercepted all that data and RANDOMISED them in such a way that it felt like a first installation for every single request.


## Navigation Menu
Navigate to Change Tracking id, Change Concurrency Value, Change Install Type and Installation Report.

![](https://user-images.githubusercontent.com/8629955/34952351-6866f27a-fa3f-11e7-8614-b80d38b30e45.png)

## Enter Tracking id and Token
Enter the flipkart affiliate tracking and api token value correctly. You can find it here, https://affiliate.flipkart.com/api/api-token

![](https://user-images.githubusercontent.com/8629955/34952348-67357a70-fa3f-11e7-9bc1-e9644d4911c8.png)

## Concurrency Value
Inorder to send mutliple request at a time, set the Concurrency value. And the request limit is the maximum limit value for the request.

![](https://user-images.githubusercontent.com/8629955/34952350-67781600-fa3f-11e7-9882-6ce736d58804.png)

## Installation Type
Direct or Fallback installation type. You can find the commission rates over here https://affiliate.flipkart.com/commissions.

![](https://user-images.githubusercontent.com/8629955/34952353-6938905a-fa3f-11e7-8f42-afaf18d622b8.png)

## Installation Report
It lets you track the Installation report based on the filter value whether Tentative, Approved or Disapproved.

![](https://user-images.githubusercontent.com/8629955/34952359-6be1d10e-fa3f-11e7-9e3f-ce5e83f111ad.png)

## Sending Request

![](https://user-images.githubusercontent.com/8629955/34952360-6c2fa8de-fa3f-11e7-921d-8447222d5264.png)

## Right Menu

![](https://user-images.githubusercontent.com/8629955/34952361-6c81fe5e-fa3f-11e7-98ae-b9ed1991d10b.png)
