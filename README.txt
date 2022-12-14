Title: Electronic Customer Records System

Author: Noah Koebe
Contact Info: nkoebe@wgu.edu
Application Version: 1.0.2
Date: 10/23/2022

IDE: IntelliJ IDEA Community Edition 2021.3.1
JDK Version: jdk-11.0.13
JavaFX Version: JavaFX-SDK-17.0.1


Directions to Run the Program:
    1. Launch the application.
    2. A log in screen will appear. Enter a username and password and click the "Submit" button. If the username/password combo is not valid, an alert will let you know.
    3. After a successful login, the application will let you know if you have any appointments in the next 15 minutes via an alert.
    4. After the alert, the Customer Search page will open. Here you can search for a Customer via their name or Customer ID. The search will look for partial names and present
    any potential match in the table. From the presented customers, you may select one and click the "Open Selected Patient File" to open the customers file and
    view more of their details/information. You may also Add a new customer by clicking the "Add New Customer" button, open the complete Appointment schedule by clicking the
    "View Appointment Schedule" button, or view the Reports page by clicking the "Generate Reports" button.
    5. If you select a patient and choose to open their file, the Customer File window will open. Here you will be shown the patients Information along with any appointments
    that the patient may have scheduled. From this page you can Add a new appointment. You can also update or delete an existing appointment by selecting it in the table and
    clicking the desired button. You can also update or delete the Customer file here as well with the labelled buttons.
    6. If you choose to update or add an appointment from a customer file directly, you will be taken to a new page where you will be able to enter the information for the appointment. A new appointment will
    be completely empty, aside from an autogenerated Appointment ID and the pre-filled Customer ID. (To create a new appointment with the customer ID of your choosing, click the Add Appointment button in
    the Appointment Schedule window. We will address this further down).
    7. Once you have entered all the necessary information, click the Save button and the information will be saved. You will be returned to the customer's file.
    Be sure not to leave any fields empty, or the application will present an alert.
    8. From the customer file, if you click the "Update Customer Info" a window will open with all the customer's information. You can change anything but the ID here.
    Again, be sure not to leave any field blank. Clicking "Save" will save the changes and return you to the customer file.
    9. Back in the Customer Search page, if you select Add New Customer, you will be taken to a window to enter all the information for the new customer. Clicking "Save"
    will save the information and return you to the Customer Search page.
    10. Clicking the "View Appointment Schedule" button on the Customer Search page will open a window displaying all appointments in the current month. At the top of the page
    you can filter which appointments you would like to see using a date picker, as well as using the radio buttons to choose between all appointments in the specified month, or all
    appointments in the specified week.
    11. On the appointment schedule page, you can Add a new appointment, update an existing appointment, or delete an existing appointment. These 3 options operate in the same way as they did
    from the Customer File page, with the difference being the Customer ID field being blank for a new appointment. The user will have to specify.
    12. From the Customer Search page, if you click the "Generate Reports" button, you will be taken to a window with 3 different report sections. The top report will show you
    the total Appointments scheduled based on month and type. You are able to choose the month and type you would like using the labelled choice boxes. The middle report will
    show you the complete schedule of any one Contact. Using the choice box, you can choose which Contact's schedule you would like to see, and the table will populate.
    13. The bottom report shows the total number of appointments from the last two weeks, as well as the total upcoming appointments in the next two weeks.
    14. The application is pretty straightforward and will alert you if anything is entered incorrectly. Read Alerts and Buttons completely.


Additional Report Information (Part A.3.f):
- For the 3rd report, I chose to present the total number of appointments in the last two weeks, as well as the total appointments scheduled in the next two weeks. This report will
help the user get an idea of how busy/productive the last two weeks were in comparison to the coming two weeks, and vice versa.

MySQL Connector Driver Version number: mysql-connector-java-8.0.30