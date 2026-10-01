ELECTRICITY BILL MANAGEMENT SYSTEM - CORE JAVA

Requirements:
- JDK 17 or newer recommended
- Windows CMD / PowerShell
- No external libraries

Features:
- Packages and sub-package
- OOP: encapsulation, inheritance, abstraction, polymorphism
- Interface + abstract class
- Collections
- Serialization/file handling
- Custom exceptions
- Multithreading
- AWT/Swing splash animation
- Console animations and ANSI colors
- Customer registration/login
- Meter readings
- Slab-based billing
- Fixed charges
- Due date and late fee calculation
- UPI/Card/Cash payment
- Bill history
- Transaction IDs
- Customer update
- Logout and persistent data

PROJECT STRUCTURE

src/
  electricity/
    main/
      ElectricityBillManagement.java
    model/
      User.java
      Customer.java
      Bill.java
      PaymentReceipt.java
    service/
      AuthService.java
      CustomerService.java
      BillService.java
      PaymentService.java
      payment/
        Payment.java
        UpiPayment.java
        CardPayment.java
        CashPayment.java
    exception/
      InvalidInputException.java
      InvalidMeterReadingException.java
      BillNotFoundException.java
      PaymentException.java
    util/
      Colors.java
      InputUtil.java
      Animation.java
      DataStore.java
      IdGenerator.java
      GuiSplash.java

data/
  users.dat
  customers.dat
  bills.dat

WINDOWS CMD BUILD

1. Open CMD.
2. Go to the project root.
3. Compile:
   javac -encoding UTF-8 -d out src\electricity\main\ElectricityBillManagement.java src\electricity\model\*.java src\electricity\service\*.java src\electricity\service\payment\*.java src\electricity\exception\*.java src\electricity\util\*.java

4. Run:
   java -cp out electricity.main.ElectricityBillManagement

If your CMD displays Unicode badly:
   chcp 65001

PowerShell:
   javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })
   java -cp out electricity.main.ElectricityBillManagement

The data directory is created automatically.
Do not delete data/*.dat unless you intentionally want to reset the application.

IMPORTANT:
This is a console-first application. AWT/Swing is used only for a short graphical startup splash/animation, while the actual application remains console based.
