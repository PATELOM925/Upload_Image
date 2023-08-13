# Upload_Image
This Android application allows users to select an image from their device and upload it to Azure Blob Storage using the Azure Storage SDK. The application utilizes the Azure Blob Storage service to store images, making it a practical choice for apps that require cloud-based storage for user-generated content.

Features:
1)Image Selection: Users can choose an image from their device using the file picker.
2)Azure Blob Storage Integration: The app integrates with Azure Blob Storage using the Azure Storage SDK for Java, allowing it to securely upload images to a specified Blob Container.
3)Asynchronous Upload: The app uses Kotlin coroutines to handle asynchronous tasks, ensuring a smooth user experience while uploading images to the cloud.
4)Error Handling: The app provides user-friendly error messages in case the image upload fails due to any reason, helping users understand the issue.

How to Use:
1)Initialization: Provide your Azure Blob Storage connection string and the desired Blob Container name in the onCreate method of the MainActivity class.
2)Image Selection: Tap the "Choose Image" button to select an image from your device.
3)Image Upload: After selecting an image, tap the "Upload Image" button to upload the selected image to the Azure Blob Storage. The app handles the upload process asynchronously.
4)Upload Status: The app displays a toast message to indicate the status of the image upload. Success or failure messages will be shown based on the outcome of the upload.
