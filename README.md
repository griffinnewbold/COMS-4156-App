# COMS-4156-App
This is the GitHub repository for the **app portion** of the Team Project associated with COMS 4156 Advanced Software Engineering. Our team name is TheJavaEngineers and the following are our members: Griffin, Mohsin, Jeannie, Michael, and Abenezer.

## Viewing the Service Repository
Please use the following link to view the repository relevant to the service: https://github.com/griffinnewbold/COMS-4156-Service

## About our App
Our app targets **healthcare workers**, it is an app that allows for the management of documents in a variety of different ways. 
All of that and how our app specifically works with our service is described in more detail below.
### App Functionality
1. Registering your Organization with our Service: Our app provides functionality for first time organizations to generate what we call a networkId which you would then paste it into the <code>NETWORK_ID</code> constant within the <code>GlobalInfo.java</code> file.
2. Registering new users with your organization: Users of the app can register with your organization using a very standard registration process, all we ask for is the following: Full Name, Email, Password, Birthday, Gender (Sex), and Profession within the organization.
3. Login to the app: Standard login procedure, requests the username and password of the specific user.
4. View a Document: Once on the user dashboard you can click on any of the presently loaded documents to view it.
    * Download the document: Click the download button and you will receive a downloaded txt file containing the document's contents.
    * Update the file: Choose a file and click upload to overwrite the current version of the document. Don't worry the old version will be saved!
    * Share the document: Choose a member of your organization, who does not currently have access to the document to be granted access to the document.
    * Download a previous version of the document: Choose which revision number you'd like, with 1 being the earliest and then click download to receive it as a txt file.
    * Compare with another document: Choose another document from the dropdown and click Compare to be shown a comparison of the two documents as well as their contents.
    * Delete the document: Click the delete button to remove the document, be careful as once you click it, there is no going back!
5. Search for a Specific Document: Type into the search bar keywords for your title and it will filter out any result that does not have at least a partial match. 
6. Upload a brand new Document: Select a file and assign it a title and click upload! NOTE: There is a maxiumum file limit of 1000KB!!!!
7. Logout: Standard Logout, returns you to the homepage of the webapp.
### How it works with our Service
To begin we must acknowledge that there is a frontend and a backend (this is true of any app but I digress) When a user performs an action on the frontend a request is sent to the backend of our app, the backend then (if necessary) makes a call to our service
and retrieves the result. The result is then processed and sent to the frontend for further process and any updates that need to occur as a result. 

## Building and Running a Local Instance
In order to build and use our service you must install the following (This guide assumes Windows but the Maven README has instructions for both Windows and Mac):

1. Maven 3.9.5: https://maven.apache.org/download.cgi Download and follow the installation instructions, be sure to set the bin as described in Maven's README as a new path variable by editing the system variables if you are on windows or by following the instructions for MacOS.
2. JDK 17: This project used JDK 17 for development so that is what we recommend you use: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
3. IntelliJ IDE: We recommend using IntelliJ but you are free to use any other IDE that you are comfortable with: https://www.jetbrains.com/idea/download/?section=windows
4. When you open IntelliJ you have the option to clone from a GitHub repo, click the green code button and copy the http line that is provided there and give it to your IDE to clone.
5. Once the entire project is cloned into a directory of your choice please open in IntelliJ as a project the <code>ClientApplication</code> folder specifically.
6. Go into <code>GlobalInfo.java</code> and alter the <code>SERVICE_IP</code> constant with the IP of the service, by default it assumes local host, if running a cloud based instance of the service please use that address instead keep the format consistent: "http://IP"

## End to End Testing
In order to properly perform end to end tests please follow the list and compare the results of the actions performed 
to the expected results provided.

## Tools used 🧰
This section includes notes on tools and technologies used in building this project, as well as any additional details if applicable.

* Firebase DB 
* Maven Package Manager
* GitHub Actions CI
  * This is enabled via the "Actions" tab on GitHub.
  * Currently, this just runs a Maven build to make sure the code builds on branch 'main'.
* Checkstyle
  * We use checkstyle on the code's backend to ensure compilance with the standards set by Google in their Java style guide
* JUnit
  * JUnit tests get run automatically as part of the CI pipeline.

## Backend Checkstyle Report

## Continuous Integration Report

## A Final Note to Developers

