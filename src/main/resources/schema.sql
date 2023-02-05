CREATE TABLE IF NOT EXISTS Fares (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       origin VARCHAR(255),
                       destination VARCHAR(255),
                       fare DECIMAL(8,2)
);
CREATE TABLE IF NOT EXISTS Taps (
                      Id INT,
                      DateTimeUTC DATETIME,
                      TapType VARCHAR(3),
                      StopId VARCHAR(50),
                      CompanyId VARCHAR(50),
                      BusID VARCHAR(50),
                      PAN VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS Trips (
                       Id INT,
                       Started DATETIME,
                       Finished DATETIME,
                       DurationSecs INT,
                       FromStopId VARCHAR(20),
                       ToStopId VARCHAR(20),
                       ChargeAmount DECIMAL(10,2),
                       CompanyId VARCHAR(20),
                       BusID VARCHAR(20),
                       PAN VARCHAR(50),
                       Status VARCHAR(20)
);